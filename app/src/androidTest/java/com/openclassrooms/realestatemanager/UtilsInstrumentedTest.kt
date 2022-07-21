package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.serialization.StringFormat
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.Method
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class UtilsInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun convertDollarToEuro() {
        val dollars =8
        val euros = Utils.convertDollarToEuro(dollars)
        assertEquals(euros,6)
    }

    @Test
    @Throws(Exception::class)
    fun convertEuroToDollar() {
        val euros = 6
        val dollars = Utils.convertEuroToDollar(euros)
        assertEquals(dollars,7)
    }

    @Test
    @Throws(Exception::class)
    fun todayDate() {
        val todayDate = Utils.todayDate

        val year: Int
        val month: Int
        val day: Int

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.time = Date()

        val dayFinal = if (day < 10){
            "0$day"
        }else{
            day.toString()
        }

        val monthFinal = if (month < 10){
            "0$month"
        }else{
            month.toString()
        }

        assertEquals( "$dayFinal/$monthFinal/$year",todayDate)

    }

    @Test
    @Throws(Exception::class)
    fun isInternetAvailable() {

        val appContext: Context = ApplicationProvider.getApplicationContext()

        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo: NetworkInfo? = cm.activeNetworkInfo
        
        assertEquals(nInfo?.isConnected, Utils.isInternetAvailable(appContext))

    }
}