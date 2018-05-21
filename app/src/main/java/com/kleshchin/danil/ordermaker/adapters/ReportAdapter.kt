package com.kleshchin.danil.ordermaker.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.utilities.inflate
import kotlinx.android.synthetic.main.item_report_recycler_view.view.*

/**
 * Created by Danil Kleshchin on 21-May-18.
 */
class ReportAdapter(val reports: ArrayList<String>) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun getItemCount() = reports.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val inflatedView = parent.inflate(R.layout.item_report_recycler_view, false)
        return ReportViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val itemPhoto = reports[position]
        holder.bindCategory(itemPhoto)
    }

    class ReportViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private lateinit var report: String

        fun bindCategory(report: String) {
            this.report = report
            view.report_message.text = report
        }
    }
}
