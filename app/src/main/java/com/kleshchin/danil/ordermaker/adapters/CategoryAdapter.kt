package com.kleshchin.danil.ordermaker.adapters

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.activities.MealActivity
import com.kleshchin.danil.ordermaker.activities.ReportActivity
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.utilities.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category_recycler_view.view.*

class CategoryAdapter() : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var languageCell = CategoryMeal(-2, "Язык",
            OrderMakerRepository.SERVER_ADDRESS + "/assets/images/language.jpg")
    private var infoCell = CategoryMeal(-1, "Информация",
            OrderMakerRepository.SERVER_ADDRESS + "/assets/images/info.jpg")
    private var reportCell: CategoryMeal = CategoryMeal(0, "Отзыв",
            OrderMakerRepository.SERVER_ADDRESS + "/assets/images/report.jpg")

    private lateinit var mealCategories: ArrayList<CategoryMeal>
    private var listener: CategoryViewHolder.OnInfoClickListener? = null

    constructor(mealCategories: ArrayList<CategoryMeal>) : this() {
        this.mealCategories = mealCategories
        this.mealCategories.add(reportCell)
        this.mealCategories.add(infoCell)
        this.mealCategories.add(languageCell)
    }

    override fun getItemCount() = mealCategories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflatedView = parent.inflate(R.layout.item_category_recycler_view, false)
        return CategoryViewHolder(inflatedView, listener!!)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val itemPhoto = mealCategories[position]
        holder.bindCategory(itemPhoto)
    }

    fun setOnInfoClickListener(listener: CategoryViewHolder.OnInfoClickListener) {
        this.listener = listener
    }

    class CategoryViewHolder(v: View, var listener: OnInfoClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private lateinit var mealCategory: CategoryMeal

        interface OnInfoClickListener {
            fun onInfoClick()
            fun onLanguageClick()
        }

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = itemView.context
            if (mealCategory.id == 0) {
                val intent = Intent(context, ReportActivity::class.java)
                context.startActivity(intent)
                return
            }

            if (mealCategory.id == -1) {
                listener.onInfoClick()
                return
            }

            if (mealCategory.id == -2) {
                listener.onLanguageClick()
                return
            }

            MealActivity.newInstance(context, mealCategory.id)
        }

        fun bindCategory(mealCategory: CategoryMeal) {
            this.mealCategory = mealCategory
            view.category_name.text = mealCategory.categoryName
            Picasso.with(view.context).load(mealCategory.categoryImageUrl).into(view.category_icon)

            var deepColor = Color.parseColor(ColorScheme.colorText)
            view.category_name.setTextColor(deepColor)
            deepColor = Color.parseColor(ColorScheme.colorAccent)
            view.vertical_view.setBackgroundColor(deepColor)
            deepColor = Color.parseColor(ColorScheme.colorItemBackground)
            view.background_card.setCardBackgroundColor(deepColor)
        }
    }
}
