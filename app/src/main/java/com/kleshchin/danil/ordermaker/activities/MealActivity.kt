package com.kleshchin.danil.ordermaker.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.adapters.MealAdapter
import com.kleshchin.danil.ordermaker.models.Meal
import kotlinx.android.synthetic.main.meal_activity.*
import android.content.Intent
import com.kleshchin.danil.ordermaker.activities.MealInfoActivity.Companion.MEAL_KEY


class MealActivity : AppCompatActivity(), MealAdapter.MealViewHolder.OnMealClickListener,
        OrderMakerRepository.OnReceiveMealInformationListener {

    private val RESULT_CODE = 1
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MealAdapter
    private var meals: ArrayList<Meal> = ArrayList()
    private var checkedMeals: ArrayList<Meal> = ArrayList()

    companion object {
        private val CATEGORY_KEY = "CATEGORY"

        fun newInstance(context: Context, categoryId: Int) {
            val categoryIntent = Intent(context, MealActivity::class.java)
            categoryIntent.putExtra(CATEGORY_KEY, categoryId)
            context.startActivity(categoryIntent)
        }
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
        pull_to_refresh.setOnRefreshListener {
            loadMeal()
        }

        loadMeal()
    }

    override fun onMealReceive(mealList: ArrayList<Meal>?) {
        pull_to_refresh.isRefreshing = false
        if (mealList == null || mealList.isEmpty()) {
            return
        }
        meals = mealList
        adapter = MealAdapter(meals)
        adapter.setOnMealCheckedListener(this)
        linearLayoutManager = LinearLayoutManager(this)
        meal_recycler_view.layoutManager = this.linearLayoutManager
        meal_recycler_view.adapter = adapter
        changeRecyclerViewVisibility()
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

    override fun onMealInfoClick(meal: Meal?) {
        val categoryIntent = MealInfoActivity.getMealInfoIntent(this, meal)
        categoryIntent.putExtra(MEAL_KEY, meal)
        startActivityForResult(categoryIntent, RESULT_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }
        if (data.getBooleanExtra(MealInfoActivity.KEY_CHECKED_STATUS, false)) {
            val meal: Meal = data.getParcelableExtra(MealInfoActivity.MEAL_KEY)
            checkedMeals.add(meal)
            (meal_recycler_view.adapter as MealAdapter).setCheckedMeal(meals.indexOf(meal))
        }
    }

    private fun loadMeal() {
        val categoryId: Int = intent.extras.getInt(CATEGORY_KEY)
        val repository = OrderMakerRepository
        repository.setOnReceiveMealInformationListener(this, this)
        repository.loadMeal(categoryId)
    }

    private fun onBasketClick() {
        val intent = OrderActivity.getOrderIntent(this, this.checkedMeals)
        startActivity(intent)
    }

    private fun changeRecyclerViewVisibility() {
        if (meals.isEmpty()) {
            pull_to_refresh.visibility = View.GONE
            meal_empty_view.visibility = View.VISIBLE
        } else {
            pull_to_refresh.visibility = View.VISIBLE
            meal_empty_view.visibility = View.GONE
        }
    }
}
