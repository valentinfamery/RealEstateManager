@file:Suppress("DEPRECATION")

package com.openclassrooms.realestatemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import java.net.InetAddress
import java.net.UnknownHostException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * 0.812).roundToInt()
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param euros
     * @return
     */
    fun convertEuroToDollar(euros: Int): Int {
        return (euros / 0.812).roundToInt()
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    val todayDate: String
        @SuppressLint("SimpleDateFormat")
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */




    fun isInternetAvailable(): Boolean {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var address: InetAddress? = null
        try {
            address = InetAddress.getByName("www.google.com")
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        return if (address == null) false else !address.toString().equals("")
    }
}