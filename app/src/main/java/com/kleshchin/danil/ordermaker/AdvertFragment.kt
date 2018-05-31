package com.kleshchin.danil.ordermaker

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kleshchin.danil.ordermaker.models.ColorScheme
import kotlinx.android.synthetic.main.fragment_advert.*

/**
 * Created by Danil Kleshchin on 31-May-18.
 */
class AdvertFragment : Fragment() {

    interface OnCompletionListener {
        fun OnCompletion()
    }

    private var listener: OnCompletionListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_advert, null)
        view?.findViewById<TextView>(R.id.advert_text)?.setTextColor(Color.parseColor(ColorScheme.colorText))
        view?.findViewById<View>(R.id.advert_video_container)?.setBackgroundColor(Color.parseColor(ColorScheme.colorBackground))
        return view
    }

    override fun onResume() {
        loadAdvertVideo()
        advert_video_view.setOnCompletionListener {
            if (listener != null) {
                listener!!.OnCompletion()
            }
        }
        super.onResume()
    }

    fun loadAdvertVideo() {
        advert_video_view.setVideoPath(OrderMakerRepository.SERVER_ADDRESS + "/assets/images/advert.mp4")
        advert_video_view.start()
    }

    override fun onStop() {
        advert_video_view.stopPlayback()
        super.onStop()
    }

    fun setOnCompletionListener(listener: OnCompletionListener) {
        this.listener = listener
    }

}
