package com.kleshchin.danil.ordermaker.utilities

import android.content.Context
import android.net.wifi.WifiManager
import android.support.annotation.RequiresPermission
import java.net.NetworkInterface
import java.util.*


/**
 * Created by Danil Kleshchin on 05-May-18.
 */
object MacAddressGetter {
    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    fun getWiFiMacAddress(context: Context): String {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wInfo = wifiManager.connectionInfo
        val macAddress = wInfo.macAddress
        return if (macAddress == "") {
            getWifiMacAddress().toUpperCase()
        } else macAddress.toUpperCase()
    }

    private fun getWifiMacAddress(): String {
        try {
            val all = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.getName().equals("wlan0", true)) continue

                val macBytes = nif.getHardwareAddress() ?: return ""

                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }

                if (res1.length > 0) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (ex: Exception) {
        }
        return "02:00:00:00:00:00"
    }
}
