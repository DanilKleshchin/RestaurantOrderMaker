package com.kleshchin.danil.ordermaker.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.facebook.stetho.Stetho
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.adapters.CategoryAdapter
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import kotlinx.android.synthetic.main.category_activity.*

class CategoryActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CategoryAdapter
    private var categories: ArrayList<CategoryMeal> = ArrayList()

    init {
        categories.add(CategoryMeal("Первое", R.drawable.first))
        categories.add(CategoryMeal("Второе", R.drawable.second))
        categories.add(CategoryMeal("Закуски", R.drawable.zakuski))
        categories.add(CategoryMeal("Напитки", R.drawable.napitki))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_activity)

        Stetho.initializeWithDefaults(this)

        setSupportActionBar(category_toolbar as Toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setHomeButtonEnabled(false)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        adapter = CategoryAdapter(categories)
        linearLayoutManager = LinearLayoutManager(this)
        category_recycler_view.layoutManager = this.linearLayoutManager
        category_recycler_view.adapter = adapter
        changeRecyclerViewVisibility()
    }
    private fun changeRecyclerViewVisibility() {
        if(category_recycler_view.visibility == VISIBLE) {
            category_recycler_view.visibility = GONE
            category_empty_view.visibility = VISIBLE
        } else {
            category_recycler_view.visibility = VISIBLE
            category_empty_view.visibility = GONE
        }
    }
}
