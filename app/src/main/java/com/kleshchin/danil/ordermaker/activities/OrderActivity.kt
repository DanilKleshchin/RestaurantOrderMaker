package com.kleshchin.danil.ordermaker.activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
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
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.order_activity.*
import java.util.*

class OrderActivity : AppCompatActivity(), View.OnClickListener {
    private val notificationId = 123
    private var meals: ArrayList<Meal> = arrayListOf()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: OrderAdapter
    private var totalAmount = 0

    companion object {
        private val MEAL_ARRAY_KEY = "MEAL_ARRAY_KEY"

        fun getOrderIntent(context: Context, meal: ArrayList<Meal>?): Intent {
            val intent = Intent(context, OrderActivity::class.java)
            return intent.putExtra(MEAL_ARRAY_KEY, meal)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_activity)

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
        createStatusNotification()
        openCategoryActivity()
    }

    private fun sendOrderToServer() {

    }

    private fun createStatusNotification() {
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this@OrderActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder =
                Notification.Builder(this@OrderActivity)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(android.R.drawable.ic_menu_info_details)
                        .setLargeIcon(BitmapFactory.decodeResource(this@OrderActivity.resources, R.mipmap.ocaka_logo_icon))
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(false)

                        .setContentTitle(getString(R.string.order_status))
                        .setStyle(Notification.BigTextStyle().bigText(getString(R.string.order_in_queue)))
                        .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            notificationBuilder.setShowWhen(true)
        }
        val notification = notificationBuilder.build()
        notification.defaults = Notification.DEFAULT_SOUND
        notification.defaults = Notification.DEFAULT_VIBRATE
        val notificationManager: NotificationManager = this@OrderActivity.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
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
