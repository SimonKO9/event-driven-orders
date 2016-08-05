package com.github.eventdrivenorders.api

import java.util

import domain.OrderStatus
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by simon on 05.08.16.
  */
class OrderStatusService(statusConsumer: KafkaConsumer[String, String],
                         statusTopic: String) {

  import com.owlike.genson.defaultGenson._

  private var confirmations: Map[Long, String] = Map()
  private var running = true

  def getStatus(orderId: Long): Option[String] = confirmations.get(orderId)

  def addPending(orderId: Long): Unit = {
    confirmations += (orderId -> "pending")
  }

  def start() = {
    statusConsumer.subscribe(util.Arrays.asList(statusTopic))

    Future {
      while (running) {
        val records = statusConsumer.poll(100)
        for (record <- records.iterator()) {
          println("New status: " + record.value())
          val status = fromJson[OrderStatus](record.value())

          val textStatus = status.success match {
            case true => "success"
            case false => "failed"
          }

          confirmations += (status.orderId -> textStatus)
        }
      }
    }
  }

  def stop() = {
    running = false
    statusConsumer.close()
  }

}
