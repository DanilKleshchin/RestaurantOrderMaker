package com.kleshchin.danil.ordermaker.utilities;

import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.models.Meal
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Danil Kleshchin on 21-May-18.
 */
object OrderJsonParser {

    @Throws(JSONException::class)
    fun parseMealJson(categoryId: Int, jsonData: String): ArrayList<Meal> {
        val mealList: ArrayList<Meal> = ArrayList()
        val jsonArray = JSONArray(jsonData)
        for (i in 0..(jsonArray.length() - 1)) {
            val jsonObject = jsonArray.getJSONObject(i)
            if (jsonObject.getInt("categoryId") == categoryId) {
                val name = jsonObject.getString("name")
                val imageUrl = OrderMakerRepository.SERVER_ADDRESS + jsonObject.getString("imageUrl")
                val price = jsonObject.getInt("price")
                val description = jsonObject.getString("description")
                mealList.add(Meal(categoryId, name, imageUrl, price, false, description, 0))
            }
        }
        return mealList
    }

    @Throws(JSONException::class)
    fun parseCategoryJson(jsonData: String): ArrayList<CategoryMeal> {
        val categoryList: ArrayList<CategoryMeal> = ArrayList()
        val jsonArray = JSONArray(jsonData)
        for (i in 0..(jsonArray.length() - 1)) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            val imageUrl = OrderMakerRepository.SERVER_ADDRESS + jsonObject.getString("imageUrl")
            val id = jsonObject.getInt("id")
            categoryList.add(CategoryMeal(id, name, imageUrl))
        }
        return categoryList
    }

    @Throws(JSONException::class)
    fun parseReportJson(jsonData: String): ArrayList<String> {
        val reportList: ArrayList<String> = ArrayList()
        val jsonArray = JSONArray(jsonData)
        for (i in 0..(jsonArray.length() - 1)) {
            reportList.add(jsonArray.getJSONObject(i).getString("text"))
        }
        return reportList
    }

    @Throws(JSONException::class)
    fun parseColorScheme(jsonData: String) {
        val jsonObject = JSONObject(jsonData)
        var colorAccent = jsonObject.getString("colorAccent")
        var colorText = jsonObject.getString("colorText")
        var colorItemBackground = jsonObject.getString("colorItemBackground")
        var colorBackground = jsonObject.getString("colorBackground")
        ColorScheme.setData(colorAccent, colorText, colorItemBackground, colorBackground)
    }
}
