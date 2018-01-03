package com.kleshchin.danil.ordermaker.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.activities.MealActivity
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import com.kleshchin.danil.ordermaker.utilities.inflate
import kotlinx.android.synthetic.main.item_category_recycler_view.view.*

class CategoryAdapter(private val mealCategories: ArrayList<CategoryMeal>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    companion object {
        private val CATEGORY_KEY = "CATEGORY"
    }

    override fun getItemCount() = mealCategories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflatedView = parent.inflate(R.layout.item_category_recycler_view, false)
        return CategoryViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val itemPhoto = mealCategories[position]
        holder.bindCategory(itemPhoto)
    }

    class CategoryViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var mealCategory: CategoryMeal? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val categoryIntent = Intent(context, MealActivity::class.java)
            categoryIntent.putExtra(CATEGORY_KEY, mealCategory)
            context.startActivity(categoryIntent)
        }

        fun bindCategory(mealCategory: CategoryMeal) {
            this.mealCategory = mealCategory
            view.category_name.text = mealCategory.name
        }
    }
}
