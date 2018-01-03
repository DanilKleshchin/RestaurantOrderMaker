package com.kleshchin.danil.ordermaker.models
import java.io.Serializable

data class CategoryMeal(var categoryName: String) : Serializable {
    var name: String = categoryName
}
