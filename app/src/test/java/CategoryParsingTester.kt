package com.kleshchin.danil.ordermaker

import com.kleshchin.danil.ordermaker.models.CategoryMeal
import com.kleshchin.danil.ordermaker.utilities.OrderJsonParser
import org.junit.Assert
import org.junit.Test

/**
 * Created by Danil Kleshchin on 24-May-18.
 */
class CategoryParsingTester {
    private val categoryCorrect = "[{" +
            "      \"id\": 1," +
            "      \"name\": \"Первое\"," +
            "      \"imageUrl\": \"/assets/images/first.jpg\"" +
            "    }]"

    private val categoryIncorrect = "[{" +
            "      \"id\": 1," +
            "      \"name\": \"Первое\"," +
            "      \"price\": 300," +                           //Показывает, что ненужные игнорятся
            "      \"imageUrl\": \"/assets/images/first.jpg\"" +
            "    }]"

    private val category = CategoryMeal(1, "Первое",
            OrderMakerRepository.SERVER_ADDRESS + "/assets/images/first.jpg")


    @Test
    fun testCorrectMealParsing() {
        testMealJsonParsing(category, OrderJsonParser.parseCategoryJson( categoryCorrect).get(0))
    }

    @Test
    fun testIncorrectMealParsing() {
        testMealJsonParsing(category, OrderJsonParser.parseCategoryJson(categoryIncorrect).get(0))
    }

    @Test
    fun showParsedMeal() {
        System.out.println(OrderJsonParser.parseCategoryJson(categoryIncorrect).get(0))
    }

    fun testMealJsonParsing(expectedMeal: CategoryMeal, actualMeal: CategoryMeal) {
        Assert.assertEquals(expectedMeal, actualMeal)
    }
}
