package com.kleshchin.danil.ordermaker.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.activities.MealInfoActivity
import com.kleshchin.danil.ordermaker.models.Meal
import com.kleshchin.danil.ordermaker.utilities.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category_recycler_view.view.*
import kotlinx.android.synthetic.main.item_meal_recycler_view.view.*

class MealAdapter(private val mealList: ArrayList<Meal>) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    companion object {
        private val MEAL_KEY = "MEAL"
    }

    private var listener: MealViewHolder.OnMealCheckedChangeListener? = null

    override fun getItemCount() = mealList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val inflatedView = parent.inflate(R.layout.item_meal_recycler_view, false)
        return MealViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val itemPhoto = mealList[position]
        holder.setOnMealCheckedListener(this.listener!!)
        holder.bindMeal(itemPhoto)
    }

    fun setOnMealCheckedListener(listener: MealViewHolder.OnMealCheckedChangeListener) {
        this.listener = listener
    }

    class MealViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var meal: Meal? = null
        private var listener: OnMealCheckedChangeListener? = null

        interface OnMealCheckedChangeListener {
            fun onMealCheckedChange(isChecked: Boolean, meal: Meal?)
        }

        init {
            v.setOnClickListener(this)
            (v.meal_check_box as CheckBox).setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.meal_check_box -> listener?.onMealCheckedChange(v.meal_check_box.isChecked, meal)
                else -> {
                    val context = itemView.context
                    val categoryIntent = Intent(context, MealInfoActivity::class.java)
                    categoryIntent.putExtra(MEAL_KEY, meal)
                    context.startActivity(categoryIntent)
                }
            }
        }

        fun setOnMealCheckedListener(listener: OnMealCheckedChangeListener) {
            this.listener = listener
        }

        fun bindMeal(meal: Meal) {
            this.meal = meal
            view.meal_name.text = meal.mealName
            Picasso.with(view.context).load(meal.mealIconUrl).into(view.meal_icon)
            view.meal_price.text = meal.mealPrice
            view.meal_check_box.isChecked = meal.isCheckedMeal
        }
    }
}
