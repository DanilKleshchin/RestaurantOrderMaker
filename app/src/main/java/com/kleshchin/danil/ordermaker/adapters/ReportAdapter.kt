package com.kleshchin.danil.ordermaker.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.kleshchin.danil.ordermaker.R
import com.kleshchin.danil.ordermaker.models.ColorScheme
import com.kleshchin.danil.ordermaker.utilities.inflate
import kotlinx.android.synthetic.main.item_report_recycler_view.view.*

/**
 * Created by Danil Kleshchin on 21-May-18.
 */
class ReportAdapter(var reports: ArrayList<String>) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun getItemCount() = reports.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val inflatedView = parent.inflate(R.layout.item_report_recycler_view, false)
        return ReportViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val itemPhoto = reports[position]
        holder.bindCategory(itemPhoto)
    }

    fun setReport(report: String) {
        reports.add(report)
        notifyDataSetChanged()
    }

    class ReportViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private lateinit var report: String

        fun bindCategory(report: String) {
            this.report = report
            view.report_message.text = report

            view.container_background.setBackgroundColor(Color.parseColor(ColorScheme.colorItemBackground))
            view.report_message.setTextColor(Color.parseColor(ColorScheme.colorText))
            view.vertical_view.setBackgroundColor(Color.parseColor(ColorScheme.colorAccent))
        }
    }
}
