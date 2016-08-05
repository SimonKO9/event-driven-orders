package com.github.eventdrivenorders.api

import com.github.eventdrivenorders.api.dto.NewOrderRequest
import com.github.eventdrivenorders.api.json.OrderFormats._
import domain.Order
import spray.httpx.SprayJsonSupport
import spray.json._
import spray.routing.HttpService

/**
  * Created by simon on 05.08.16.
  */
trait ApiRoutes extends HttpService with SprayJsonSupport {

  def submitOrder(order: Order): Unit

  def getStatus(orderId: Long): Option[String]

  val routes = path("orders") {
    post {
      entity(as[NewOrderRequest]) { order =>
        complete {
          val orderId = OrderIDGenerator.next
          println(s"New order request: $orderId $order")

          submitOrder(Order(orderId, order.products))
          JsObject(("orderId", JsNumber(orderId)))
        }
      }
    }
  } ~ path("orders" / LongNumber) { orderId =>
    get {
      complete {
        getStatus(orderId).map { status =>
          JsObject(("orderId", JsNumber(orderId)), ("status", JsString(status)))
        }
      }
    }
  }


}
