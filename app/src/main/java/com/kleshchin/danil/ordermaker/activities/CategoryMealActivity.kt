package com.kleshchin.danil.ordermaker.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kleshchin.danil.ordermaker.R

class CategoryMealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_meal_activity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
