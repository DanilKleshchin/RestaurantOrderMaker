package com.kleshchin.danil.ordermaker.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import com.facebook.stetho.Stetho
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.adapters.CategoryAdapter
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.models.Order
import com.kleshchin.danil.ordermaker.utilities.MacAddressGetter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import android.graphics.Color.parseColor
import com.squareup.picasso.NetworkPolicy


class CategoryActivity : AppCompatActivity(), OrderMakerRepository.OnReceiveCategoryInformationListener,
        OrderMakerRepository.OnOrderStatusListener, OrderMakerRepository.OnReceiveColorSchemeListener {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CategoryAdapter
    private var categories: ArrayList<CategoryMeal> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_activity)

        Stetho.initializeWithDefaults(this)

        setSupportActionBar(category_toolbar as Toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setHomeButtonEnabled(false)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        val url = OrderMakerRepository.SERVER_ADDRESS + OrderMakerRepository.DESIGN_ADDRESS
        Picasso.with(this).load(url).networkPolicy(NetworkPolicy.NO_CACHE).into(toolbar_logo_image)


        OrderMakerRepository.setOnOrderStatusListener(this)
        OrderMakerRepository.setOnReceiveColorSchemeListener(this)
        OrderMakerRepository.loadColorScheme()

        pull_to_refresh.setOnRefreshListener {
            loadCategory()
            OrderMakerRepository.SERVER_ADDRESS + OrderMakerRepository.DESIGN_ADDRESS
            Picasso.with(this).invalidate(url)
            Picasso.with(this).load(url).networkPolicy(NetworkPolicy.NO_CACHE).into(toolbar_logo_image)
            OrderMakerRepository.loadColorScheme()
        }

        loadCategory()
    }

    override fun onCategoryReceive(categoryList: ArrayList<CategoryMeal>?) {
        pull_to_refresh.isRefreshing = false
        if (categoryList == null || categoryList.isEmpty()) {
            return
        }
        categories = categoryList
        linearLayoutManager = LinearLayoutManager(this)
        category_recycler_view.layoutManager = this.linearLayoutManager
        adapter = CategoryAdapter(categoryList)
        category_recycler_view.adapter = adapter
        changeRecyclerViewVisibility()
    }

    override fun onColorSchemeReceive() {
        var deepColor = Color.parseColor(ColorScheme.colorAccent)
        top_view.setBackgroundColor(deepColor)
        pull_to_refresh.setBackgroundColor(Color.parseColor(ColorScheme.colorBackground))
        category_empty_view.indeterminateDrawable.setColorFilter(
                Color.parseColor(ColorScheme.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN)
        category_toolbar.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
        category_recycler_view.adapter?.notifyDataSetChanged()
    }

    override fun onOrderStatusReceive(orderList: ArrayList<Order>?) {
        if (orderList == null) {
            return
        }
        var message = ""
        for (order in orderList) {
            message += order.mealName + " " + order.toString() + "\n"
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialog_title)
        builder.setMessage(message)
        builder.setNegativeButton("OK") { dialog, id ->
            // No need action
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /*val inflater = menuInflater
        inflater.inflate(R.menu.status_menu, menu)*/
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.status_menu -> onStatusClick()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun onStatusClick() {
        OrderMakerRepository.getOrderStatus(MacAddressGetter.getWiFiMacAddress(this))
    }

    private fun loadCategory() {
        val repository = OrderMakerRepository
        repository.setOnReceiveCategoryInformationListener(this, this)
        repository.loadCategory()
    }

    private fun changeRecyclerViewVisibility() {
        if (categories.isEmpty()) {
            pull_to_refresh.visibility = GONE
            category_empty_view.visibility = VISIBLE
        } else {
            pull_to_refresh.visibility = VISIBLE
            category_empty_view.visibility = GONE
        }
    }

    override fun onBackPressed() {

    }
}
