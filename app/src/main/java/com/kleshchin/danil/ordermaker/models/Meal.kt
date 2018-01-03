package com.kleshchin.danil.ordermaker.models
import java.io.Serializable

data class Meal(var name: String, var iconUrl: String, var price: String,
                var isChecked: Boolean) : Serializable {
    var mealName: String = name
    var mealIconUrl: String = iconUrl
    var mealPrice: String = price
    var isCheckedMeal: Boolean = isChecked
}
