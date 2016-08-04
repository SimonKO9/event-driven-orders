package com.github.simonthecat.eventdrivenorders.orderservice

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer

import scala.io.StdIn

/**
  * Created by simon on 04.08.16.
  */
object OrderApp extends App {

  val confirmationService = new ConfirmationService(
    confirmationConsumer = new KafkaConsumer[String, String](kafka.storeConfirmationConsumer),
    confirmationTopic = "order.confirmation",
    replyProducer = new KafkaProducer[String, String](kafka.producerCfg),
    replyTopic = "api.reply"
  )

  val orderService = new OrderProcessingService(
    new KafkaConsumer[String, String](kafka.orderConsumerCfg),
    "order.order",
    new KafkaProducer[String, String](kafka.producerCfg),
    "store.update"
  )

  confirmationService.start()
  orderService.start()

  StdIn.readLine()
  confirmationService.stop()
  orderService.stop()
}
