package com.kleshchin.danil.ordermaker.models
import java.io.Serializable

data class CategoryMeal(var categoryName: String, var categoryImage: Int) : Serializable {
    var name: String = categoryName
    var image: Int = categoryImage
}
