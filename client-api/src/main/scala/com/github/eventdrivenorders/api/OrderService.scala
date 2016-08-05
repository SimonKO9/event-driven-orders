package com.github.eventdrivenorders.api

import domain.Order
import org.apache.kafka.clients.producer.{ProducerRecord, KafkaProducer}
import com.github.eventdrivenorders.api.json.OrderFormats._
import spray.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by simon on 05.08.16.
  */
class OrderService(orderProducer: KafkaProducer[String, String],
                   orderTopic: String,
                   orderStatusService: OrderStatusService) {

  def submit(order: Order) = Future {
    println(s"Submitting order: $order")
    val orderJson = order.toJson.toString()
//    orderStatusService.addPending(order.id)
    orderProducer.send(new ProducerRecord[String, String](orderTopic, orderJson))
  }

}
