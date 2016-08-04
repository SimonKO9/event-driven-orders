package com.github.simonthecat.eventdrivenorders.productservice

import domain.ProductAndCount

import scala.collection.immutable.Seq

/**
  * Created by simon on 04.08.16.
  */
trait ProductsService {

  def quantity(id: String): Long

  def removeProducts(productsAndCounts: Seq[ProductAndCount]): Boolean

}
