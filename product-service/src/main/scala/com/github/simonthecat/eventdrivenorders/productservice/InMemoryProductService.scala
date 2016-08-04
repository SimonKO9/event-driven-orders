package com.github.simonthecat.eventdrivenorders.productservice

import domain._

import scala.collection.immutable.Seq

/**
  * Created by simon on 04.08.16.
  */
class InMemoryProductService extends ProductsService {

  private var store = Map[Product, Long](
    Product("apple") -> 3,
    Product("orange") -> 10
  )

  override def quantity(productId: String): Long = synchronized {
    store.getOrElse(Product(productId), 0)
  }

  override def removeProducts(productsAndCounts: Seq[ProductAndCount]): Boolean = synchronized {
    val allProductsAvailable = productsAndCounts.forall {
      case ProductAndCount(Product(id), count) => quantity(id) >= count
    }

    if (allProductsAvailable) {
      store = store.map {
        case (product, count) =>
          val newCount = store(product) - productsAndCounts.find(_.product == product).map(_.count).getOrElse(0L)
          (product, newCount)
      }
      true
    } else {
      false
    }
  }

}
