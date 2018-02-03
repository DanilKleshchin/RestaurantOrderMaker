package com.kleshchin.danil.ordermaker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.adapters.MealAdapter
import com.kleshchin.danil.ordermaker.models.Meal
import kotlinx.android.synthetic.main.meal_activity.*


class MealActivity : AppCompatActivity(), MealAdapter.MealViewHolder.OnMealCheckedChangeListener,
        OrderMakerRepository.OnReceiveMealInformationListener {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MealAdapter
    private var meals: ArrayList<Meal> = ArrayList()
    private var checkedMeals: ArrayList<Meal> = ArrayList()


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

        val repository = OrderMakerRepository(this, this)
        repository.loadMeal()
    }

    override fun onMealReceive(mealList: ArrayList<Meal>) {
        meals = mealList
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
        when (menuItem.itemId) {
            android.R.id.home -> finish()
            R.id.basket -> onBasketClick()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun onBasketClick() {
        val intent = OrderActivity.getOrderIntent(this, this.checkedMeals)
        startActivity(intent)
    }
}
