package com.kleshchin.danil.ordermaker.models

/**
 * Created by Danil Kleshchin on 29-May-18.
 */
object ColorScheme {
    var colorAccent: String = "#101010"
    var colorText: String = "#101010"
    var colorItemBackground = "#101010"
    var colorBackground = "#101010"

    fun setData(colorAccent: String, colorText: String, colorItemBackground: String, colorBackground: String) {
        this.colorAccent = colorAccent
        this.colorText = colorText
        this.colorItemBackground = colorItemBackground
        this.colorBackground = colorBackground
    }
}
