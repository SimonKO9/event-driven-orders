package com.github.eventdrivenorders.api

import akka.actor.ActorSystem
import com.github.eventdrivenorders.api.json.OrderFormats
import domain.Order
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import spray.routing.SimpleRoutingApp

/**
  * Created by simon on 05.08.16.
  */
object ApiApp extends App with SimpleRoutingApp with ApiRoutes with OrderFormats {

  implicit val system = ActorSystem("orders-api-system")

  val orderStatusService = new OrderStatusService(new KafkaConsumer[String, String](kafka.orderStatusConsumer), "api.reply")

  val orderService = new OrderService(new KafkaProducer[String, String](kafka.producerCfg), "order.order", orderStatusService)

  override def submitOrder(order: Order): Unit = orderService.submit(order)

  override def getStatus(orderId: Long): Option[String] = orderStatusService.getStatus(orderId)

  orderStatusService.start()
  startServer("localhost", 8080, "order-api-actor")(routes)

}
