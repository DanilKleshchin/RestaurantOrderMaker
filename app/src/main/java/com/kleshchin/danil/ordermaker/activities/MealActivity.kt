package com.kleshchin.danil.ordermaker.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.adapters.MealAdapter
import com.kleshchin.danil.ordermaker.models.Meal
import kotlinx.android.synthetic.main.meal_activity.*


class MealActivity : AppCompatActivity(), MealAdapter.MealViewHolder.OnMealCheckedChangeListener {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MealAdapter
    private var meals: ArrayList<Meal> = ArrayList()
    private var checkedMeals: ArrayList<Meal> = ArrayList()

    init {
        val url = "https://cdn.pixabay.com/photo/2015/06/19/23/04/spaghetti-815385_960_720.jpg"
        val sushi = "http://www.xarakiri.ru/upload/iblock/97f/97f8f455544c87b3d1ab5b0079fb16f1.jpg"
        val wok = "https://www.makitao.ru/userfiles/menu/big_butaniku_ydon_lapsha_wok_krasnodar_envdtf9.jpg"
        val pizza = "http://supercook.ru/images-pizza/page-pizza-pastry-04.jpg"
        val funchoza = "https://i.ytimg.com/vi/TVaCQs5rS6c/maxresdefault.jpg"
        val shaurma = "https://www.edimdoma.ru/data/ckeditor_pictures/27454/content_fotolia_119231708_subscription_xxl.jpg"
        meals.add(Meal("Суши", sushi, "300 руб", false))
        meals.add(Meal("Вок", wok, "500 руб", false))
        meals.add(Meal("Фунчоза", funchoza, "200 руб", false))
        meals.add(Meal("Пицца", pizza, "400 руб", false))
        meals.add(Meal("Шаурма", shaurma, "350 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Суши", sushi, "300 руб", false))
        meals.add(Meal("Вок", wok, "500 руб", false))
        meals.add(Meal("Фунчоза", funchoza, "200 руб", false))
        meals.add(Meal("Пицца", pizza, "400 руб", false))
        meals.add(Meal("Шаурма", shaurma, "350 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_activity)

        setSupportActionBar(meal_toolbar as Toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        adapter = MealAdapter(meals)
        adapter.setOnMealCheckedListener(this)
        linearLayoutManager = LinearLayoutManager(this)
        meal_recycler_view.layoutManager = this.linearLayoutManager
        meal_recycler_view.adapter = adapter
    }

    override fun onMealCheckedChange(isChecked: Boolean, meal: Meal?) {
        if (meal == null) {
            return
        }

        if (isChecked) {
            checkedMeals.add(meal)
        } else {
            checkedMeals.remove(meal)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.meal_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            android.R.id.home -> finish()
            R.id.basket -> onBasketClick()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun onBasketClick() {
        val intent: Intent = Intent(this, OrderActivity::class.java)
        startActivity(intent)
    }
}
