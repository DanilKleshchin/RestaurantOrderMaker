package com.kleshchin.danil.ordermaker.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.models.Meal
import com.kleshchin.danil.ordermaker.utilities.CircleTransform
import com.kleshchin.danil.ordermaker.utilities.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_meal_recycler_view.view.*

class MealAdapter(private val mealList: ArrayList<Meal>) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {


    private var listener: MealViewHolder.OnMealClickListener? = null

    override fun getItemCount() = mealList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val inflatedView = parent.inflate(R.layout.item_meal_recycler_view, false)
        return MealViewHolder(inflatedView, listener!!)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val itemMeal = mealList[position]
        holder.bindMeal(itemMeal)
    }

    fun setOnMealCheckedListener(listener: MealViewHolder.OnMealClickListener) {
        this.listener = listener
    }

    fun setCheckedMeal(position: Int) {
        mealList[position].isChecked = true
        notifyItemChanged(position)
    }

    class MealViewHolder(v: View, listener: OnMealClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var meal: Meal? = null
        private var listener: OnMealClickListener? = listener

        interface OnMealClickListener {
            fun onMealCheckedChange(isChecked: Boolean, meal: Meal?)
            fun onMealInfoClick(meal: Meal?)
        }

        init {
            v.setOnClickListener(this)
            (v.meal_minus as TextView).setOnClickListener(this)
            (v.meal_plus as TextView).setOnClickListener(this)
            (v.meal_count_number as TextView).setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v.id) {

                R.id.meal_minus -> {
                    var count = meal?.count
                    if (count != null &&count != 0) {
                        meal?.count = --count
                    }
                    view.meal_count_number.text = count.toString()
                    if (count != 0) {
                        this.meal!!.isChecked = true
                        listener?.onMealCheckedChange(true, meal)
                    } else {
                        this.meal!!.isChecked = false
                        listener?.onMealCheckedChange(false, meal)
                    }
                }
                R.id.meal_plus -> {
                    this.meal!!.isChecked = true
                    ++meal!!.count
                    view.meal_count_number.text = meal!!.count.toString()
                    listener?.onMealCheckedChange(true, meal)
                }
                else -> {
                    listener?.onMealInfoClick(meal)
                }
            }
        }

        fun bindMeal(meal: Meal) {
            this.meal = meal
            view.meal_name.text = meal.name
            Picasso.with(view.context).load(meal.iconUrl).transform(CircleTransform()).into(view.meal_icon)
            view.meal_price.text = String.format(view.context.resources.getString(R.string.meal_price), meal.price)
            view.meal_count_number.text = meal.count.toString()

            var deepColor = Color.parseColor(ColorScheme.colorText)
            view.meal_name.setTextColor(deepColor)
            view.meal_price.setTextColor(deepColor)
            deepColor = Color.parseColor(ColorScheme.colorAccent)
            view.meal_minus.setTextColor(deepColor)
            view.meal_plus.setTextColor(deepColor)
            view.meal_count_number.setTextColor(deepColor)
            deepColor = Color.parseColor(ColorScheme.colorItemBackground)
            view.card_view_background.setCardBackgroundColor(deepColor)
        }
    }
}
