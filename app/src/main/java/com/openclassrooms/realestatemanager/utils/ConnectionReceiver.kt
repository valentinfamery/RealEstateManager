package com.openclassrooms.realestatemanager.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel

class ConnectionReceiver(var realEstateViewModel: RealEstateViewModel) : BroadcastReceiver() {

    var offlineMode = false

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("onReceive()","onReceive()")
            val isDeviceConnected: Boolean = Utils.isInternetAvailable(context)

            if (!isDeviceConnected && !offlineMode) {
                offlineMode = true
                Log.e("passage", "vers-hors-ligne")
            } else if (isDeviceConnected && offlineMode) {
                offlineMode = false
                Log.e("passage", "vers-en-ligne")
                realEstateViewModel.refreshRealEstates()
            }
    }
}