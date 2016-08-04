package com.github.simonthecat.eventdrivenorders.productservice

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer

import scala.io.StdIn

/**
  * Created by simon on 04.08.16.
  */
object ProductApp extends App {

  val productsService: ProductsService = new InMemoryProductService

  val orderConfirmationService = new OrderConfirmationService(
    productsService = productsService,
    kafkaProducer = new KafkaProducer[String, String](kafka.confirmationProducerCfg),
    kafkaTopic = "order.confirmation"
  )

  val productUpdateCountService = new ProductUpdateCountService(
    productsService = productsService,
    orderConfirmationService = orderConfirmationService,
    kafkaConsumer = new KafkaConsumer[String, String](kafka.orderConsumerCfg),
    kafkaTopic = "store.update"
  )


  productUpdateCountService.start()
  StdIn.readLine()
  productUpdateCountService.stop()
}
