package com.kleshchin.danil.ordermaker

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import com.kleshchin.danil.ordermaker.models.Meal
import com.kleshchin.danil.ordermaker.models.Order
import com.kleshchin.danil.ordermaker.provider.DatabaseHelper
import com.kleshchin.danil.ordermaker.provider.OrderMakerProvider
import com.kleshchin.danil.ordermaker.utilities.OrderJsonParser
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.lang.ref.WeakReference


object OrderMakerRepository : LoaderManager.LoaderCallbacks<Cursor> {

    private val CATEGORY_CODE = 0
    private val MEAL_CODE = 1
    private val TAG = "OrderMakerRepository"
    private val OKHTTP_TAG = "OrderMakerRepository"
    val SERVER_ADDRESS = "http://192.168.0.100:3000"
    val DESIGN_ADDRESS = "/assets/images/design/logo.png"

    private var categoryListener: OnReceiveCategoryInformationListener? = null
    private var mealListener: OnReceiveMealInformationListener? = null
    private var orderStatusListener: OnOrderStatusListener? = null
    private var onReportReceiveListener: OnReportReceiveListener? = null
    private var onReceiveColorSchemeListener: OnReceiveColorSchemeListener? = null
    private var onReceiveCompositionListener: OnReceiveCompositionListener? = null
    private var context: WeakReference<Context>? = null

    interface OnReceiveCategoryInformationListener {
        fun onCategoryReceive(categoryList: ArrayList<CategoryMeal>?)
    }

    interface OnReceiveColorSchemeListener {
        fun onColorSchemeReceive()
    }

    interface OnReceiveMealInformationListener {
        fun onMealReceive(mealList: ArrayList<Meal>?)
    }

    interface OnOrderStatusListener {
        fun onOrderStatusReceive(orderList: ArrayList<Order>?)
    }

    interface OnReportReceiveListener {
        fun onReportReceive(reportList: ArrayList<String>?)
    }

    interface OnReceiveCompositionListener {
        fun onReceiveComposition(composition: String)
    }

    fun setOnReceiveMealInformationListener(context: Context, listener: OnReceiveMealInformationListener) {
        this.context = WeakReference(context)
        mealListener = listener
    }

    fun setOnReceiveCategoryInformationListener(context: Context, listener: OnReceiveCategoryInformationListener) {
        this.context = WeakReference(context)
        categoryListener = listener
    }

    fun setOnOrderStatusListener(listener: OnOrderStatusListener) {
        orderStatusListener = listener
    }

    fun setOnReportReceiveListener(listener: OnReportReceiveListener) {
        onReportReceiveListener = listener
    }

    fun setOnReceiveColorSchemeListener(listener: OnReceiveColorSchemeListener) {
        onReceiveColorSchemeListener = listener
    }

    fun setOnReceiveCompositionListener(listener: OnReceiveCompositionListener) {
        onReceiveCompositionListener = listener
    }

    fun loadCategory() {
        val categoryLoader = InfoDownloader(InfoDownloader.Models.Category)
        categoryLoader.execute()
    }

    fun loadReport() {
        ReportDownloader().execute()
    }

    fun loadMeal(categoryId: Int) {
        val loader = InfoDownloader(InfoDownloader.Models.Meal, categoryId)
        loader.execute()
    }

    fun loadColorScheme() {
        ColorSchemeDownloader().execute()
    }

    fun loadComposition(name: String) {
        CompositionDownloader(name).execute()
    }

    fun sendOrder(order: Order) {
        Thread {
            val client = OkHttpClient()
            val requestBody = FormBody.Builder()
                    .add("mealName", order.mealName)
                    .add("macAddress", order.mac)
                    .add("number", order.number.toString())
                    .add("status", Order.OrderStatus.Queue.toString())
                    .build()
            val request = Request.Builder()
                    .url(SERVER_ADDRESS + "/order")
                    .post(requestBody)
                    .build()
            Log.i(OKHTTP_TAG, "POST " + request.url() + " " + requestBody.toString())
            val response = client.newCall(request).execute()
            if (!response.isSuccessful()) {
                Log.e(TAG, response.toString())
            }
        }.start()
    }

    fun sendReport(report: String) {
        Thread {
            val client = OkHttpClient()
            val requestBody = FormBody.Builder()
                    .add("text", report)
                    .build()
            val request = Request.Builder()
                    .url(SERVER_ADDRESS + "/report")
                    .post(requestBody)
                    .build()
            Log.i(OKHTTP_TAG, "POST " + request.url() + " " + requestBody.toString())
            val response = client.newCall(request).execute()
            if (!response.isSuccessful()) {
                Log.e(TAG, response.toString())
            }
        }.start()
    }

    fun getOrderStatus(macAddress: String) {
        OrderDownloader(macAddress).execute()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        when (id) {
            CATEGORY_CODE -> return CursorLoader(context?.get(), OrderMakerProvider.createUrlForTable(DatabaseHelper.CATEGORY_TABLE), null, null, null, null)
            MEAL_CODE -> return CursorLoader(context?.get(), OrderMakerProvider.createUrlForTable(DatabaseHelper.MEAL_TABLE), null, null, null, null)
            else -> throw IllegalArgumentException("no id handed")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        val loaderId = loader!!.id
        when (loaderId) {
            CATEGORY_CODE -> {
                categoryListener!!.onCategoryReceive(DatabaseHelper.createCategoryFromCursor(data!!))
            }
            MEAL_CODE -> {
                mealListener!!.onMealReceive(DatabaseHelper.createMealFromCursor(data!!))
            }
            else -> throw IllegalArgumentException("no loader id handled!")
        }
        data.close()
        (context?.get() as AppCompatActivity).loaderManager.destroyLoader(loaderId)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {

    }

    private class InfoDownloader(var model: Models, var id: Int = -1) : AsyncTask<Void, Void, Void>() {
        enum class Models {
            Meal, Category
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            when (model) {
                Models.Category -> {
                    loadCategory()
                }
                Models.Meal -> {
                    loadMeal(id)
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            var tableCode: Int = -1
            when (model) {
                Models.Meal -> {
                    tableCode = MEAL_CODE
                }
                Models.Category -> {
                    tableCode = CATEGORY_CODE
                }
            }
            (context?.get() as AppCompatActivity).supportLoaderManager.restartLoader(tableCode, null, OrderMakerRepository)
            super.onPostExecute(result)
        }

        private fun loadCategory() {
            try {
                val url = SERVER_ADDRESS + "/category"
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url(url)
                        .build()
                var responses: Response? = null
                try {
                    responses = client.newCall(request).execute()
                    Log.i(OKHTTP_TAG, "Load url " + url)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val jsonData = responses?.body()?.string() ?: return

                val resolver = context?.get()?.contentResolver
                if (resolver != null) {
                    DatabaseHelper.insertCategoryList(OrderJsonParser.parseCategoryJson(jsonData), resolver)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        private fun loadMeal(categoryId: Int) {
            try {
                val url = SERVER_ADDRESS + "/meal"
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url(url)
                        .build()
                var responses: Response? = null
                try {
                    responses = client.newCall(request).execute()
                    Log.i(OKHTTP_TAG, "Load url " + url)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val jsonData = responses?.body()?.string() ?: return

                val resolver = context?.get()?.contentResolver
                if (resolver != null) {
                    DatabaseHelper.insertMealList(OrderJsonParser.parseMealJson(categoryId, jsonData), resolver)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private class OrderDownloader(var macAddress: String) : AsyncTask<Void, Void, ArrayList<Order>?>() {
        override fun doInBackground(vararg p0: Void?): ArrayList<Order>? {
            return loadOrderStatus()

        }

        override fun onPostExecute(result: ArrayList<Order>?) {

            orderStatusListener!!.onOrderStatusReceive(result)
            super.onPostExecute(result)
        }

        private fun loadOrderStatus(): ArrayList<Order>? {
            try {
                val url = SERVER_ADDRESS + "/order"
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url(url)
                        .build()
                var responses: Response? = null
                try {
                    responses = client.newCall(request).execute()
                    Log.i(OKHTTP_TAG, "Load url " + url)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val orderList: ArrayList<Order> = ArrayList()
                val jsonData = responses?.body()?.string() ?: return null
                Log.i(OKHTTP_TAG, jsonData)
                val jsonArray = JSONArray(jsonData)
                for (i in 0..(jsonArray.length() - 1)) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val mac = jsonObject.getString("macAddress")
                    if (mac == macAddress) {
                        val name = jsonObject.getString("mealName")
                        val number = jsonObject.getLong("number")
                        val status = Order.OrderStatus.valueOf(jsonObject.getString("status"))
                        orderList.add(Order(name, mac, number, status))
                    }
                }
                return orderList
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return null
        }

    }

    private class ReportDownloader : AsyncTask<Void, Void, ArrayList<String>?>() {

        override fun doInBackground(vararg p0: Void?): ArrayList<String>? {
            return loadOrderStatus()

        }

        override fun onPostExecute(result: ArrayList<String>?) {

            onReportReceiveListener!!.onReportReceive(result)
            super.onPostExecute(result)
        }

        private fun loadOrderStatus(): ArrayList<String>? {
            try {
                val url = SERVER_ADDRESS + "/report"
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url(url)
                        .build()
                var responses: Response? = null
                try {
                    responses = client.newCall(request).execute()
                    Log.i(OKHTTP_TAG, "Load url " + url)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val jsonData = responses?.body()?.string() ?: return null
                Log.i(OKHTTP_TAG, jsonData)
                return OrderJsonParser.parseReportJson(jsonData)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return null
        }
    }

    private class ColorSchemeDownloader : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg p0: Void?): Void? {
            loadColorScheme()
            return null
        }

        override fun onPostExecute(result: Void?) {
            onReceiveColorSchemeListener!!.onColorSchemeReceive()
            super.onPostExecute(result)
        }

        private fun loadColorScheme() {
            try {
                val url = SERVER_ADDRESS + "/assets/images/design/color_scheme.json"
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url(url)
                        .build()
                var responses: Response? = null
                try {
                    responses = client.newCall(request).execute()
                    Log.i(OKHTTP_TAG, "Load url " + url)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val jsonData = responses?.body()?.string() ?: return
                Log.i(OKHTTP_TAG, jsonData)
                OrderJsonParser.parseColorScheme(jsonData)
                return
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private class CompositionDownloader(var id: String) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg p0: Void?): String? {
            return loadColorScheme()
        }

        override fun onPostExecute(result: String?) {
            onReceiveCompositionListener!!.onReceiveComposition(result!!)
            super.onPostExecute(result)
        }

        private fun loadColorScheme(): String {
            try {
                val url = SERVER_ADDRESS + "/composition?mealId=" + id
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url(url)
                        .build()
                var responses: Response? = null
                try {
                    responses = client.newCall(request).execute()
                    Log.i(OKHTTP_TAG, "Load url " + url)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val jsonData = responses?.body()?.string() ?: return ""
                Log.i(OKHTTP_TAG, jsonData)
                return OrderJsonParser.parseComposition(jsonData)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}
