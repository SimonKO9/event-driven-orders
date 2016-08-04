package com.github.simonthecat.eventdrivenorders.productservice

import domain.UpdateStoreStatus
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * Created by simon on 04.08.16.
  */
class OrderConfirmationService(productsService: ProductsService,
                               kafkaProducer: KafkaProducer[String, String],
                               kafkaTopic: String) {

  import com.owlike.genson.defaultGenson._

  def confirm(orderId: Long) = kafkaProducer.send(new ProducerRecord(kafkaTopic, toJson(UpdateStoreStatus(orderId, true))))

  def fail(orderId: Long) = kafkaProducer.send(new ProducerRecord(kafkaTopic, toJson(UpdateStoreStatus(orderId, false))))

}
