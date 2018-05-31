package com.kleshchin.danil.ordermaker.models

/**
 * Created by Danil Kleshchin on 29-May-18.
 */
object ColorScheme {
    var colorAccent: String = "#ffffff"
    var colorText: String = "#ffffff"
    var colorItemBackground = "#51637a"
    var colorBackground = "#101010"

    fun setData(colorAccent: String, colorText: String, colorItemBackground: String, colorBackground: String) {
        this.colorAccent = colorAccent
        this.colorText = colorText
        this.colorItemBackground = colorItemBackground
        this.colorBackground = colorBackground
    }
}
