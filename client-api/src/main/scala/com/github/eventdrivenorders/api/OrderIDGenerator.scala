package com.github.eventdrivenorders.api

import java.util.concurrent.atomic.AtomicLong
import java.util.prefs.Preferences

/**
  * This is very naive, but sufficient for POC purposes.
  *
  * Created by simon on 05.08.16.
  */
object OrderIDGenerator {

  private val prefs = Preferences.userNodeForPackage(OrderIDGenerator.getClass)

  private val atomicLong = new AtomicLong(prefs.getLong("order_seq", 1L))

  def next: Long = {
    val value = atomicLong.incrementAndGet()
    synchronized {
      prefs.putLong("order_seq", value)
      value
    }
  }

}
