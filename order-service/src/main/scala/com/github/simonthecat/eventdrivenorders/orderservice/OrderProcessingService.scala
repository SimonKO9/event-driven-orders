package com.github.simonthecat.eventdrivenorders.orderservice

import java.util

import domain.Order
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.collection.JavaConversions._
import scala.util.Try

/**
  * Created by simon on 04.08.16.
  */
class OrderProcessingService(orderConsumer: KafkaConsumer[String, String],
                             orderConsumerTopic: String,
                             storeUpdateProducer: KafkaProducer[String, String],
                             storeUpdateTopic: String) {

  import com.owlike.genson.defaultGenson._

  var running = true

  def start() = {
    orderConsumer.subscribe(util.Arrays.asList(orderConsumerTopic))

    while (running) {
      val records = orderConsumer.poll(100)
      records.iterator().foreach(processOrder)
    }
  }

  def processOrder(record: ConsumerRecord[String, String]): Unit = {
    println(s"Processing ${record.value()}")

    for {
      order <- Try(fromJson[Order](record.value()))
      _ <- Try {
        println(s"Sending to store service: $order")
        storeUpdateProducer.send(new ProducerRecord[String, String](storeUpdateTopic, toJson(order)))
      }
    } yield Unit

    println(s"Processing ${record.value()}")

  }

  def stop() = {
    orderConsumer.close()
    running = false
  }
}
