package com.kleshchin.danil.ordermaker.activities

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.adapters.OrderAdapter
import com.kleshchin.danil.ordermaker.models.Meal
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.models.Order
import com.kleshchin.danil.ordermaker.utilities.MacAddressGetter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.order_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class OrderActivity : AppCompatActivity(), View.OnClickListener {
    private val notificationId = 123
    private var meals: ArrayList<Meal> = arrayListOf()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: OrderAdapter
    private var totalAmount = 0
    private lateinit var macAddress: String

    companion object {
        private const val MEAL_ARRAY_KEY = "MEAL_ARRAY_KEY"

        fun getOrderIntent(context: Context, meal: ArrayList<Meal>?): Intent {
            val intent = Intent(context, OrderActivity::class.java)
            return intent.putExtra(MEAL_ARRAY_KEY, meal)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_activity)

        macAddress = MacAddressGetter.getWiFiMacAddress(this)

        setSupportActionBar(order_toolbar as Toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        button_place_order.setOnClickListener(this)
        meals = intent.getParcelableArrayListExtra(MEAL_ARRAY_KEY)
        adapter = OrderAdapter(meals)
        linearLayoutManager = LinearLayoutManager(this)
        order_recycler_view.layoutManager = this.linearLayoutManager
        order_recycler_view.adapter = adapter
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = order_recycler_view.adapter as OrderAdapter
                totalAmount -= meals[viewHolder.adapterPosition].price
                adapter.removeAt(viewHolder.adapterPosition)
                order_total.text = String.format(resources.getString(R.string.order_total), totalAmount)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(order_recycler_view)

        totalAmount = meals.sumBy { it.price }
        order_total.text = String.format(resources.getString(R.string.order_total), totalAmount)

        val url = OrderMakerRepository.SERVER_ADDRESS + OrderMakerRepository.DESIGN_ADDRESS
        Picasso.with(this).load(url).into(toolbar_logo_image)

        top_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        middle_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        bottom_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        order_text_view.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
        order_text_view.setTextColor(Color.parseColor(ColorScheme.colorText))
        order_total.setTextColor(Color.parseColor(ColorScheme.colorText))
        order_total.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
        button_place_order.setTextColor(Color.parseColor(ColorScheme.colorText))
        button_place_order.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
        order_toolbar.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
        (order_toolbar as Toolbar).getNavigationIcon()?.setColorFilter(Color.parseColor(ColorScheme.colorAccent), PorterDuff.Mode.SRC_ATOP)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                cancelNotification()
                finish()
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            button_place_order -> {
                onPlaceOrderButtonClick()
            }
        }
    }

    private fun onPlaceOrderButtonClick() {
        if (totalAmount == 0) {
            return
        }
        sendOrderToServer()
        openCategoryActivity()
    }

    private fun sendOrderToServer() {
        if (meals.isEmpty()) {
            return
        }
        for (meal in meals) {
            val order = Order(meal.name, macAddress, System.currentTimeMillis(), Order.OrderStatus.Queue)
            OrderMakerRepository.sendOrder(order)
        }
    }

    private fun openCategoryActivity() {
        val intent = Intent(this@OrderActivity, CategoryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun cancelNotification() {
        val notificationManager: NotificationManager = this@OrderActivity.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    abstract class SwipeToDeleteCallback(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete_icon)
        private val intrinsicWidth = deleteIcon.intrinsicWidth
        private val intrinsicHeight = deleteIcon.intrinsicHeight
        private val background = ColorDrawable()
        private val backgroundColor = Color.parseColor("#f44336")

        override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
            return false
        }

        override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top

            // Draw the red delete background
            background.color = backgroundColor
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + intrinsicHeight

            // Draw the delete icon
            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

}
