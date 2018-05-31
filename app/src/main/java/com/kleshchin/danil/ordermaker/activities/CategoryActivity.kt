package com.kleshchin.danil.ordermaker.activities

import android.app.Dialog
import android.content.Intent
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
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.facebook.stetho.Stetho
import com.kleshchin.danil.ordermaker.AdvertFragment
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.adapters.CategoryAdapter
import com.kleshchin.danil.ordermaker.models.CategoryMeal
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.models.Order
import com.kleshchin.danil.ordermaker.utilities.MacAddressGetter
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class CategoryActivity : AppCompatActivity(), OrderMakerRepository.OnReceiveCategoryInformationListener,
        OrderMakerRepository.OnOrderStatusListener, OrderMakerRepository.OnReceiveColorSchemeListener, AdvertFragment.OnCompletionListener, CategoryAdapter.CategoryViewHolder.OnInfoClickListener {

    companion object {
        val KEY_SHOW_ADVERT = "KEY_SHOW_ADVERT"
    }

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CategoryAdapter
    private var categories: ArrayList<CategoryMeal> = ArrayList()
    private val advertFragment = AdvertFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_activity)

        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.getBoolean(KEY_SHOW_ADVERT)) {
                showAdvert()
            }
        }

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
        adapter.setOnInfoClickListener(this)
        category_recycler_view.adapter = adapter
        changeRecyclerViewVisibility()
    }

    fun showAdvert() {
        category_container.visibility = GONE
        fragmentManager.beginTransaction()
                .replace(R.id.advert_container, advertFragment)
                .commit()
        advertFragment.setOnCompletionListener(this)
    }

    override fun onInfoClick() {
        onStatusClick()
    }

    override fun onLanguageClick() {
        showRadioButtonDialog()
    }

    override fun OnCompletion() {
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.advert_container)).commit()
        category_container.visibility = VISIBLE
    }

    fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        val refresh = Intent(this, CategoryActivity::class.java)
        startActivity(refresh)
        finish()
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

    private fun showRadioButtonDialog() {

        // custom dialog
        val dialog = Dialog(this)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите язык")
        builder.setNegativeButton("OK") { dialog, id ->

        }
        dialog.setContentView(R.layout.language_picker)
        val stringList = ArrayList<String>()  // here is list
        for (i in 0..4) {
            stringList.add("RadioButton " + (i + 1))
        }
        val rg: RadioGroup = dialog.findViewById(R.id.radio_group)
        val button: Button = dialog.findViewById(R.id.button_ok)
        button.setOnClickListener {
            val radioButtonID = rg.getCheckedRadioButtonId()
            val radioButton: RadioButton = rg.findViewById(radioButtonID)
            val idx = rg.indexOfChild(radioButton)
            when (idx) {
                0 -> setLocale("RU")
                1 -> setLocale("EN")
            }

        }

        dialog.show()

    }
}
