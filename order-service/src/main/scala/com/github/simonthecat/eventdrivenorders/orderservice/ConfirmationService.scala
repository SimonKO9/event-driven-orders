package com.github.simonthecat.eventdrivenorders.orderservice

import java.util

import domain.{OrderStatus, UpdateStoreStatus}
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

/**
  * Created by simon on 04.08.16.
  */
class ConfirmationService(confirmationConsumer: KafkaConsumer[String, String],
                          confirmationTopic: String,
                          replyProducer: KafkaProducer[String, String],
                          replyTopic: String) {

  import com.owlike.genson.defaultGenson._

  var running = true

  def start() = {
    confirmationConsumer.subscribe(util.Arrays.asList(confirmationTopic))
    Future {
      while (running) {
        val records = confirmationConsumer.poll(100)
        records.iterator().foreach(processConfirmation)
      }
    }.recover {
      case ex => ex.printStackTrace()
    }
  }

  def processConfirmation(record: ConsumerRecord[String, String]): Unit = {
    println(s"Processing ${record.value()}")

    for {
      status <- Try(fromJson[UpdateStoreStatus](record.value()))
      _ <- Try {
        println(s"Replying $status")
        replyProducer.send(new ProducerRecord(replyTopic, toJson(OrderStatus(status.orderId, status.success))))
      }
    } yield Unit

    println(s"Processed ${record.value()}")
  }

  def stop() = {
    confirmationConsumer.close()
    running = false
  }
}
