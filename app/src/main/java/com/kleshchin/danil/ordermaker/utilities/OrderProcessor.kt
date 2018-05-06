package com.kleshchin.danil.ordermaker.utilities

import com.kleshchin.danil.ordermaker.models.Order

/**
 * Created by Danil Kleshchin on 06-May-18.
 */
object OrderProcessor {
    fun getOrderList(): Order {
        return Order("testName", "TestMac", 1_111L, Order.OrderStatus.Queue)
    }
}
