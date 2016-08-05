package com.github.eventdrivenorders.api.json

import com.github.eventdrivenorders.api.dto.NewOrderRequest
import domain._
import spray.json.DefaultJsonProtocol

/**
  * Created by simon on 05.08.16.
  */
trait OrderFormats extends DefaultJsonProtocol {

  implicit def productFormat = jsonFormat1(Product)

  implicit def productAndCountFormat = jsonFormat2(ProductAndCount)

  implicit def orderFormat = jsonFormat2(Order)

  implicit def newOrderRequestFormat = jsonFormat1(NewOrderRequest)

}

object OrderFormats extends OrderFormats