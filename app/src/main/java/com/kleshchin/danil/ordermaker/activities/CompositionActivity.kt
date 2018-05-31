package com.kleshchin.danil.ordermaker.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.models.Meal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.composition_activity.*
import kotlinx.android.synthetic.main.toolbar.*

class CompositionActivity : AppCompatActivity(), OrderMakerRepository.OnReceiveCompositionListener {

    private var meal: Meal? = null

    companion object {
        val MEAL_KEY = "MEAL_KEY"

        fun getMealCompositionIntent(context: Context, meal: Meal?): Intent {
            val intent = Intent(context, CompositionActivity::class.java)
            return intent.putExtra(MEAL_KEY, meal)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.composition_activity)


        setSupportActionBar(meal_composition_toolbar as Toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        val url = OrderMakerRepository.SERVER_ADDRESS + OrderMakerRepository.DESIGN_ADDRESS
        Picasso.with(this).load(url).into(toolbar_logo_image)


        this.meal = intent.getParcelableExtra(MEAL_KEY)
        meal_info_meal_name.text = this.meal?.name

        OrderMakerRepository.loadComposition(meal!!.id.toString())
        OrderMakerRepository.setOnReceiveCompositionListener(this)
        top_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        middle_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        container_background.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
        meal_info_meal_name.setTextColor(Color.parseColor(ColorScheme.colorText))
        meal_info_info.setTextColor(Color.parseColor(ColorScheme.colorText))
        meal_composition_toolbar.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))

        (meal_composition_toolbar as Toolbar).getNavigationIcon()?.setColorFilter(Color.parseColor(ColorScheme.colorAccent), PorterDuff.Mode.SRC_ATOP)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onReceiveComposition(composition: String) {
        meal_info_info.text = composition
    }
}
