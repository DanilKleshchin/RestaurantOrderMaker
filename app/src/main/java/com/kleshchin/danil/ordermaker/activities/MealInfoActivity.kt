package com.kleshchin.danil.ordermaker.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.models.Meal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.meal_info_activity.*

class MealInfoActivity : AppCompatActivity(), View.OnClickListener {
    private var meal: Meal? = null

    companion object {
        val MEAL_KEY = "MEAL_KEY"
        var KEY_CHECKED_STATUS = "KEY_CHECKED_STATUS"

        fun getMealInfoIntent(context: Context, meal: Meal?): Intent {
            val intent = Intent(context, MealInfoActivity::class.java)
            return intent.putExtra(MEAL_KEY, meal)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_info_activity)

        setSupportActionBar(meal_info_toolbar as Toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        button_add_to_basket.setOnClickListener(this)
        this.meal = intent.getParcelableExtra(MEAL_KEY)
        meal_info_meal_name.text = this.meal?.name
        Picasso.with(this).load(this.meal?.iconUrl).into(meal_info_meal_icon)
        meal_info_info.text = this.meal?.info
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            button_add_to_basket -> {
                val intent = Intent()
                intent.putExtra(KEY_CHECKED_STATUS, true)
                intent.putExtra(MEAL_KEY, meal)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
