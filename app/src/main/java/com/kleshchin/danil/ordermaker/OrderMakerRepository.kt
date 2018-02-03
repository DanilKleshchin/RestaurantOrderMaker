package com.kleshchin.danil.ordermaker

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import com.kleshchin.danil.ordermaker.models.Meal
import com.kleshchin.danil.ordermaker.provider.DatabaseHelper
import com.kleshchin.danil.ordermaker.provider.OrderMakerProvider


class OrderMakerRepository : LoaderManager.LoaderCallbacks<Cursor> {

    private var context: Context? = null
    private val CATEGORY_CODE = 0
    private val MEAL_CODE = 1

    private var categoryListener: OnReceiveCategoryInformationListener? = null
    private var mealListener: OnReceiveMealInformationListener? = null

    interface OnReceiveCategoryInformationListener {
        fun onCategoryReceive(categoryList: ArrayList<CategoryMeal>)
    }

    interface OnReceiveMealInformationListener {
        fun onMealReceive(mealList: ArrayList<Meal>)
    }

    constructor(context: Context, listener: OnReceiveCategoryInformationListener) {
        categoryListener = listener
        this.context = context
    }

    constructor(context: Context, listener: OnReceiveMealInformationListener) {
        mealListener = listener
        this.context = context
        //insertValues()
    }

    fun loadCategory() {
        (context as AppCompatActivity).supportLoaderManager.restartLoader(CATEGORY_CODE, null, this)
    }

    fun loadMeal() {
        (context as AppCompatActivity).supportLoaderManager.restartLoader(MEAL_CODE, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        when (id) {
            CATEGORY_CODE -> return CursorLoader(context, OrderMakerProvider.createUrlForTable(DatabaseHelper.CATEGORY_TABLE), null, null, null, null)
            MEAL_CODE -> return CursorLoader(context, OrderMakerProvider.createUrlForTable(DatabaseHelper.MEAL_TABLE), null, null, null, null)
            else -> throw IllegalArgumentException("no id handed")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        val loaderId = loader!!.id
        when (loaderId) {
            CATEGORY_CODE -> {
                categoryListener!!.onCategoryReceive(DatabaseHelper.createCategoryFromCursor(data!!))
            }
            MEAL_CODE ->  {
                mealListener!!.onMealReceive(DatabaseHelper.createMealFromCursor(data!!))
            }
            else -> throw IllegalArgumentException("no loader id handled!")
        }
        (context as AppCompatActivity).loaderManager.destroyLoader(loaderId)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {

    }

    private fun insertValues() {
        val meals: ArrayList<Meal> = ArrayList()
        val url = "https://cdn.pixabay.com/photo/2015/06/19/23/04/spaghetti-815385_960_720.jpg"
        val sushi = "http://www.xarakiri.ru/upload/iblock/97f/97f8f455544c87b3d1ab5b0079fb16f1.jpg"
        val wok = "https://www.makitao.ru/userfiles/menu/big_butaniku_ydon_lapsha_wok_krasnodar_envdtf9.jpg"
        val pizza = "http://supercook.ru/images-pizza/page-pizza-pastry-04.jpg"
        val funchoza = "https://i.ytimg.com/vi/TVaCQs5rS6c/maxresdefault.jpg"
        val shaurma = "https://www.edimdoma.ru/data/ckeditor_pictures/27454/content_fotolia_119231708_subscription_xxl.jpg"
        val description = "Суши, или как на них говорят японцы — суси — традиционное блюдо японской кухни," +
                " главным ингредиентом которого является рис. В наших краях принято все виды суши " +
                "называть «суши», однако любимое блюдо у нас – это роллы, настоящее название которых " +
                "— макиили макидзуси, что в переводе означает — закрученные суши. Таким образом можно" +
                " на следующий вопрос: Чем отличаются роллы от суши? Суши — небольшой комок, специально" +
                " приготовленного риса, сформированный руками, на который положен небольшой кусочек рыбы."
        meals.add(Meal("Суши", sushi, 300, false, description))
        meals.add(Meal("Вок", wok, 500, false, description))
        meals.add(Meal("Фунчоза", funchoza, 200, false, description))
        meals.add(Meal("Пицца", pizza, 400, false, description))
        meals.add(Meal("Шаурма", shaurma, 350, false, description))
        meals.add(Meal("Паста", url, 100, false, description))
        meals.add(Meal("Суши", sushi, 300, false, description))
        meals.add(Meal("Вок", wok, 500, false, description))
        meals.add(Meal("Фунчоза", funchoza, 200, false, description))
        meals.add(Meal("Пицца", pizza, 400, false, description))
        meals.add(Meal("Шаурма", shaurma, 350, false, description))
        meals.add(Meal("Паста", url, 100, false, description))
        val resolver = context!!.contentResolver
        resolver.bulkInsert(OrderMakerProvider.createUrlForTable(DatabaseHelper.MEAL_TABLE),
                DatabaseHelper.createMealContentValues(meals))
    }

}
