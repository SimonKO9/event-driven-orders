package com.github.eventdrivenorders.api.dto

import domain.ProductAndCount

/**
  * Created by simon on 05.08.16.
  */
case class NewOrderRequest(products: List[ProductAndCount])
