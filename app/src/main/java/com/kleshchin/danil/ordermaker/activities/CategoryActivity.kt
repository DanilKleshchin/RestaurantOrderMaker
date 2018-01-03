package com.kleshchin.danil.ordermaker.activities

import CategoryAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import kotlinx.android.synthetic.main.category_activity.*

class CategoryActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CategoryAdapter
    var categories: ArrayList<CategoryMeal> = ArrayList()

    init {
        categories.add(CategoryMeal("Первое"))
        categories.add(CategoryMeal("Второе"))
        categories.add(CategoryMeal("Закуски"))
        categories.add(CategoryMeal("Напитки"))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_activity)

        adapter = CategoryAdapter(categories)
        linearLayoutManager = LinearLayoutManager(this)
        category_recycler_view.layoutManager = this.linearLayoutManager
        category_recycler_view.adapter = adapter
    }
}
