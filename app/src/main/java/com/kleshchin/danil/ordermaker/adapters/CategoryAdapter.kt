import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.activities.CategoryMealActivity
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import kotlinx.android.synthetic.main.item_category_recycler_view.view.*

class CategoryAdapter(private val mealCategories: ArrayList<CategoryMeal>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    override fun getItemCount() = mealCategories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.item_category_recycler_view, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPhoto = mealCategories[position]
        holder.bindCategory(itemPhoto)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var mealCategory: CategoryMeal? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val categoryIntent = Intent(context, CategoryMealActivity::class.java)
            categoryIntent.putExtra(PHOTO_KEY, mealCategory)
            context.startActivity(categoryIntent)
        }

        fun bindCategory(mealCategory: CategoryMeal) {
            this.mealCategory = mealCategory
            view.category_name.text = mealCategory.name
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }
}
