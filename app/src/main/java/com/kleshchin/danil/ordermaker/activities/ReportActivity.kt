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

/**
 * Created by Danil Kleshchin on 21-May-18.
 */
class ReportActivity: AppCompatActivity(), OrderMakerRepository.OnReportReceiveListener {

    private var reports: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_activity)
        Stetho.initializeWithDefaults(this)

        setSupportActionBar(report_toolbar as Toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setHomeButtonEnabled(false)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        OrderMakerRepository.setOnReportReceiveListener(this)

        report_pull_to_refresh.setOnRefreshListener {
            loadReport()
        }

        loadReport()
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
}
