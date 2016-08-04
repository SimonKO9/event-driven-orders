package domain

import scala.collection.immutable.Seq

case class Product(id: String)

case class ProductAndCount(product: Product, count: Long)

case class Order(id: Long, products: Seq[ProductAndCount])