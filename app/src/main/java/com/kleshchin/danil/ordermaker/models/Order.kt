package com.kleshchin.danil.ordermaker.models

import java.io.Serializable

/**
 * Created by Danil Kleshchin on 05-May-18.
 */
data class Order(var mealName: String, var mac: String, var number: Long, var status: OrderStatus) : Serializable {
    public enum class OrderStatus {
        Queue, Progress, Done;
    }

    override fun toString(): String {
        return when(status) {
            OrderStatus.Queue -> "В очереди"
            OrderStatus.Progress -> "На выполнении"
            OrderStatus.Done -> "Готово"
        }
    }
}
