package com.kleshchin.danil.ordermaker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View.GONE
import android.view.View.VISIBLE
import com.facebook.stetho.Stetho
import com.kleshchin.danil.ordermaker.OrderMakerRepository
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.adapters.ReportAdapter
import kotlinx.android.synthetic.main.report_activity.*
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.toolbar.*


/**
 * Created by Danil Kleshchin on 21-May-18.
 */
class ReportActivity: AppCompatActivity(), OrderMakerRepository.OnReportReceiveListener, View.OnClickListener {

    private var reports: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_activity)
        Stetho.initializeWithDefaults(this)

        setSupportActionBar(report_toolbar as Toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        add_report_fab.setOnClickListener(this)
        val url = OrderMakerRepository.SERVER_ADDRESS + OrderMakerRepository.DESIGN_ADDRESS
        Picasso.with(this).load(url).into(toolbar_logo_image)

        OrderMakerRepository.setOnReportReceiveListener(this)

        report_pull_to_refresh.setOnRefreshListener {
            loadReport()
        }

        loadReport()

        report_toolbar.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
        (report_toolbar as Toolbar).navigationIcon?.setColorFilter(Color.parseColor(ColorScheme.colorAccent), PorterDuff.Mode.SRC_ATOP)
        top_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        container_background.setBackgroundColor(Color.parseColor(ColorScheme.colorBackground))
        report_empty_view.indeterminateDrawable.setColorFilter(
                Color.parseColor(ColorScheme.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        add_report_fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor(ColorScheme.colorAccent))
    }

    override fun onReportReceive(reportList: ArrayList<String>?) {
        report_pull_to_refresh.isRefreshing = false
        if (reportList == null || reportList.isEmpty()) {
            return
        }
        this.reports = reportList
        var linearLayoutManager = LinearLayoutManager(this)
        report_recycler_view.layoutManager = linearLayoutManager
        var adapter = ReportAdapter(reports)
        report_recycler_view.adapter = adapter
        changeRecyclerViewVisibility()
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.add_report_fab) {
            onAddReportClick()
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun loadReport() {
        OrderMakerRepository.loadReport()
    }

    private fun changeRecyclerViewVisibility() {
        if (reports.isEmpty()) {
            report_pull_to_refresh.visibility = GONE
            report_empty_view.visibility = VISIBLE
        } else {
            report_pull_to_refresh.visibility = VISIBLE
            report_empty_view.visibility = GONE
        }
    }

    private fun onAddReportClick() {
        val reportEditText = EditText(this)
        reportEditText.maxLines = 6

        reportEditText.hint = "Введите ваше сообщение"

        AlertDialog.Builder(this)
                .setTitle("Отзыв о ресторане")
                .setView(reportEditText)
                .setPositiveButton("Ок", DialogInterface.OnClickListener { dialog, whichButton ->
                    val report = reportEditText.text.toString()
                    sendReport(report)
                })
                .setNegativeButton("Отмена", DialogInterface.OnClickListener { dialog, whichButton -> })
                .show()
    }

    private fun sendReport(report: String) {
        (report_recycler_view.adapter as ReportAdapter).setReport(report)
        OrderMakerRepository.sendReport(report)
    }
}
