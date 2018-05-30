package com.kleshchin.danil.ordermaker.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.activities.MealInfoActivity
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.models.Meal
import com.kleshchin.danil.ordermaker.utilities.inflate
import kotlinx.android.synthetic.main.item_order_recycler_view.view.*

class OrderAdapter(private val mealList: ArrayList<Meal>?) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    companion object {
        private val MEAL_KEY = "MEAL"
    }

    override fun getItemCount(): Int {
        if (mealList != null) {
            return mealList.size
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflatedView = parent.inflate(R.layout.item_order_recycler_view, false)
        return OrderViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        if (mealList == null) {
            return
        }
        val itemMeal = mealList[position]
        holder.bindMeal(itemMeal)
    }

    fun removeAt(position: Int) {
        mealList?.removeAt(position)
        notifyDataSetChanged()
    }

    class OrderViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var meal: Meal? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val categoryIntent = MealInfoActivity.getMealInfoIntent(v.context, this.meal)
            categoryIntent.putExtra(MEAL_KEY, meal)
            context.startActivity(categoryIntent)
        }

        fun bindMeal(meal: Meal) {
            this.meal = meal
            view.order_meal_name.text = meal.name
            view.order_meal_price.text = String.format(view.context.resources.getString(R.string.meal_price), meal.price)
            view.order_position.text = (adapterPosition + 1).toString()
            view.order_meal_count.text = meal.count.toString()

            view.container_background.setCardBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
            view.order_position.setTextColor(Color.parseColor(ColorScheme.colorText))
            view.order_meal_name.setTextColor(Color.parseColor(ColorScheme.colorText))
            view.order_meal_price.setTextColor(Color.parseColor(ColorScheme.colorText))

        }
    }
}
