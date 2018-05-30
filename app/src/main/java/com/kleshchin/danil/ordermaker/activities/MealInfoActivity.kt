package com.kleshchin.danil.ordermaker.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.models.Meal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.meal_info_activity.*
import kotlinx.android.synthetic.main.toolbar.*

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

        val url = OrderMakerRepository.SERVER_ADDRESS + OrderMakerRepository.DESIGN_ADDRESS
        Picasso.with(this).load(url).into(toolbar_logo_image)

        button_add_to_basket.setOnClickListener(this)
        this.meal = intent.getParcelableExtra(MEAL_KEY)
        meal_info_meal_name.text = this.meal?.name
        Picasso.with(this).load(this.meal?.iconUrl).into(meal_info_meal_icon)
        meal_info_info.text = this.meal?.info

        top_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        middle_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        bottom_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        container_background.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
        meal_info_meal_name.setTextColor(Color.parseColor(ColorScheme.colorText))
        meal_info_info.setTextColor(Color.parseColor(ColorScheme.colorText))
        button_add_to_basket.setTextColor(Color.parseColor(ColorScheme.colorText))
        button_add_to_basket.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
        meal_info_toolbar.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))

        (meal_info_toolbar as Toolbar).getNavigationIcon()?.setColorFilter(Color.parseColor(ColorScheme.colorAccent), PorterDuff.Mode.SRC_ATOP)
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
