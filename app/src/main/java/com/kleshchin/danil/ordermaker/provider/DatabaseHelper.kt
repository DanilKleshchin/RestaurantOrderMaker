package com.kleshchin.danil.ordermaker.provider

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import com.kleshchin.danil.ordermaker.models.Meal


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "OrderMaker"

        val KEY_ID = "_id"

        val CATEGORY_TABLE = "Category"
        val KEY_CATEGORY_ID = "Category_Id"
        val KEY_CATEGORY_NAME = "Category_Name"
        val KEY_CATEGORY_ICON_URL = "Category_Icon_Url"

        val MEAL_TABLE = "Meal"
        val KEY_MEAL_NAME = "Meal_Name"
        val KEY_MEAL_ICON_URL = "Meal_Icon_Url"
        val KEY_MEAL_PRICE = "Meal_Price"
        val KEY_MEAL_INFO = "Meal_Info"
        val KEY_MEAL_CATEGORY_ID = "Meal_Category_Id"
        val KEY_MEAL_COUNT = "Meal_Count"


        fun createCategoryContentValues(categories: ArrayList<CategoryMeal>): Array<ContentValues> {
            val contentValuesArray: Array<ContentValues> = Array(categories.size, { ContentValues() })
            for (i in categories.indices) {
                val contentValues = ContentValues()
                contentValues.put(KEY_CATEGORY_NAME, categories[i].categoryName)
                contentValues.put(KEY_CATEGORY_ICON_URL, categories[i].categoryImageUrl)
                contentValues.put(KEY_CATEGORY_ID, categories[i].id)
                contentValuesArray[i] = contentValues
            }
            return contentValuesArray
        }

        fun createMealContentValues(categories: ArrayList<Meal>): Array<ContentValues> {
            val contentValuesArray: Array<ContentValues> = Array(categories.size, { ContentValues() })
            for (i in categories.indices) {
                val contentValues = ContentValues()
                contentValues.put(KEY_MEAL_NAME, categories[i].name)
                contentValues.put(KEY_MEAL_ICON_URL, categories[i].iconUrl)
                contentValues.put(KEY_MEAL_PRICE, categories[i].price)
                contentValues.put(KEY_MEAL_INFO, categories[i].info)
                contentValues.put(KEY_MEAL_CATEGORY_ID, categories[i].categoryId)
                contentValues.put(KEY_MEAL_COUNT, categories[i].count)
                contentValues.put(KEY_ID, categories[i].id)
                contentValuesArray[i] = contentValues
            }
            return contentValuesArray
        }

        fun createCategoryFromCursor(data: Cursor): ArrayList<CategoryMeal>? {
            val competitions = ArrayList<CategoryMeal>()
            if (data.moveToFirst()) {
                do {
                    val name = data.getString(data.getColumnIndex(KEY_CATEGORY_NAME))
                    val iconUrl = data.getString(data.getColumnIndex(KEY_CATEGORY_ICON_URL))
                    val id = data.getInt(data.getColumnIndex(KEY_CATEGORY_ID))
                    val meal = CategoryMeal(id, name, iconUrl)
                    competitions.add(meal)
                } while (data.moveToNext())
                return competitions
            }
            return null
        }

        fun createMealFromCursor(data: Cursor): ArrayList<Meal>? {
            val competitions = ArrayList<Meal>()
            if (data.moveToFirst()) {
                do {
                    val name = data.getString(data.getColumnIndex(KEY_MEAL_NAME))
                    val iconUrl = data.getString(data.getColumnIndex(KEY_MEAL_ICON_URL))
                    val price = data.getInt(data.getColumnIndex(KEY_MEAL_PRICE))
                    val info = data.getString(data.getColumnIndex(KEY_MEAL_INFO))
                    val categoryId = data.getInt(data.getColumnIndex(KEY_MEAL_CATEGORY_ID))
                    val count = data.getInt(data.getColumnIndex(KEY_MEAL_COUNT))
                    val id = data.getInt(data.getColumnIndex(KEY_ID))
                    val meal = Meal(categoryId, name, iconUrl, price, false, info, count, id)
                    competitions.add(meal)
                } while (data.moveToNext())
                return competitions
            }
            return null
        }

        fun insertCategoryList(categoryList: ArrayList<CategoryMeal>, resolver: ContentResolver) {
            val uri = OrderMakerProvider.createUrlForTable(DatabaseHelper.CATEGORY_TABLE)
            resolver.delete(uri, null, null)
            resolver.bulkInsert(uri, DatabaseHelper.createCategoryContentValues(categoryList))
        }

        fun insertMealList(mealList: ArrayList<Meal>, resolver: ContentResolver) {
            val uri = OrderMakerProvider.createUrlForTable(DatabaseHelper.MEAL_TABLE)
            resolver.delete(uri, null, null)
            resolver.bulkInsert(uri, DatabaseHelper.createMealContentValues(mealList))
        }
    }

    private val TEXT_TYPE = " TEXT"
    private val INT_TYPE = " INTEGER"
    private val COMMA_SEP = ","
    private val BRACKET_RIGHT_SEP = ")"
    private val BRACKET_LEFT_SEP = "("

    private val CREATE_CATEGORY_TABLE = "CREATE TABLE " + CATEGORY_TABLE + BRACKET_LEFT_SEP +
            KEY_CATEGORY_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            KEY_CATEGORY_NAME + TEXT_TYPE + COMMA_SEP +
            KEY_CATEGORY_ICON_URL + TEXT_TYPE + BRACKET_RIGHT_SEP

    private val CREATE_MEAL_TABLE = "CREATE TABLE " + MEAL_TABLE + BRACKET_LEFT_SEP +
            KEY_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            KEY_MEAL_CATEGORY_ID + INT_TYPE + COMMA_SEP +
            KEY_MEAL_NAME + TEXT_TYPE + COMMA_SEP +
            KEY_MEAL_ICON_URL + TEXT_TYPE + COMMA_SEP +
            KEY_MEAL_PRICE + INT_TYPE + COMMA_SEP +
            KEY_MEAL_COUNT + INT_TYPE + COMMA_SEP +
            KEY_MEAL_INFO + TEXT_TYPE + BRACKET_RIGHT_SEP

    private val DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS " + CATEGORY_TABLE
    private val DROP_MEAL_TABLE = "DROP TABLE IF EXISTS " + CATEGORY_TABLE

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_CATEGORY_TABLE)
        p0?.execSQL(CREATE_MEAL_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_CATEGORY_TABLE)
        p0?.execSQL(DROP_MEAL_TABLE)
    }
}
