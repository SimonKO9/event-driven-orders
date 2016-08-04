package com.github.simonthecat.eventdrivenorders.productservice

import java.util

import domain.Order
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

/**
  * Created by simon on 04.08.16.
  */
class ProductUpdateCountService(productsService: ProductsService,
                                orderConfirmationService: OrderConfirmationService,
                                kafkaConsumer: KafkaConsumer[String, String],
                                kafkaTopic: String) {

  import com.owlike.genson.defaultGenson._

  private var running = true

  def start() = {
    kafkaConsumer.subscribe(util.Arrays.asList(kafkaTopic))

    Future {
      while (running) {
        val records = kafkaConsumer.poll(100)
        for (record <- records.iterator()) {
          for {
            order <- Try(fromJson[Order](record.value()))
            _ <- processOrder(order)
          } yield Unit
        }
      }
    }
  }

  def processOrder(order: Order): Try[Unit] = Try {
    println(s"Processing order: $order")

    productsService.removeProducts(order.products) match {
      case true => orderConfirmationService.confirm(order.id)
      case false => orderConfirmationService.fail(order.id)
    }

    println(s"Processed order: $order")
  }

  def stop() = {
    running = false
    kafkaConsumer.close()
  }

}
