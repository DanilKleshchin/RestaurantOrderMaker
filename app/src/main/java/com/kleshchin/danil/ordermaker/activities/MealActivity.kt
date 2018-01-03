package com.kleshchin.danil.ordermaker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
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
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
        meals.add(Meal("Паста", url, "100 руб", false))
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
        Log.d("AHA", checkedMeals.size.toString())
    }
}
