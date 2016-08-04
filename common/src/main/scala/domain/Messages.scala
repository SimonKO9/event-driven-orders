package domain

case class UpdateStoreStatus(orderId: Long, success: Boolean)

case class OrderStatus(orderId: Long, success: Boolean)