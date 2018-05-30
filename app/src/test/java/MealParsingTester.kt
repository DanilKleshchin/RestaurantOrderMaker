package com.kleshchin.danil.ordermaker

import com.kleshchin.danil.ordermaker.models.Meal
import com.kleshchin.danil.ordermaker.utilities.OrderJsonParser
import org.junit.Assert
import org.junit.Test

/**
 * Created by Danil Kleshchin on 21-May-18.
 */
class MealParsingTester {

    private val mealCorrect = "[{\n" +
            "      \"id\": 1," +
            "      \"categoryId\": \"3\"," +
            "      \"name\": \"Суши\"," +
            "      \"price\": 300," +
            "      \"imageUrl\": \"/assets/images/sushi.jpg\"," +
            "      \"description\": \"Суши, или как на них говорят японцы ...\"" +
            "    }]"

    private val mealIncorrect = "[{\n" +
            "      \"id\": 1," +
            "      \"categoryId\": \"3\"," +
            "      \"mealName\": \"Суши\"," +
            "      \"price\": 300," +
            "      \"imageUrl\": \"/assets/images/sushi.jpg\"," +
            "      \"description\": \"Суши, или как на них говорят японцы ...\"" +
            "    }]"

    private val meal = Meal(3, "Суши", OrderMakerRepository.SERVER_ADDRESS +
            "/assets/images/sushi.jpg", 300, false, "Суши, или как на них говорят японцы ...")


    @Test
    fun testCorrectMealParsing() {
        testMealJsonParsing(meal, OrderJsonParser.parseMealJson(3, mealCorrect).get(0))
    }

    @Test
    fun testIncorrectMealParsing() {
        testMealJsonParsing(meal, OrderJsonParser.parseMealJson(3, mealIncorrect).get(0))
    }

    fun testMealJsonParsing(expectedMeal: Meal, actualMeal: Meal) {
        Assert.assertEquals(expectedMeal, actualMeal)
    }

    @Test
    fun showParsedMeal() {
        System.out.println(OrderJsonParser.parseMealJson(3, mealCorrect).get(0))
    }
}
