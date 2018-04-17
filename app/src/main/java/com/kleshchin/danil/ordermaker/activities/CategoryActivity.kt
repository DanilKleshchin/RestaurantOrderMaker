package com.kleshchin.danil.ordermaker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View.GONE
import android.view.View.VISIBLE
import com.facebook.stetho.Stetho
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.adapters.CategoryAdapter
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import kotlinx.android.synthetic.main.category_activity.*

class CategoryActivity : AppCompatActivity(), OrderMakerRepository.OnReceiveCategoryInformationListener {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CategoryAdapter

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

        val repository = OrderMakerRepository
        repository.setOnReceiveCategoryInformationListener(this, this)
        repository.loadCategory()
    }

    override fun onCategoryReceive(categoryList: ArrayList<CategoryMeal>?) {
        if(categoryList == null || categoryList.isEmpty()) {
            return
        }
        linearLayoutManager = LinearLayoutManager(this)
        category_recycler_view.layoutManager = this.linearLayoutManager
        adapter = CategoryAdapter(categoryList)
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
