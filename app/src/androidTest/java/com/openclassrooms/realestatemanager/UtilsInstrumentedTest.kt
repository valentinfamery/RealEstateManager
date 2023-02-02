package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
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
    fun checkNotInternet() {
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
        Thread.sleep(5000)
        assertFalse(Utils.isInternetAvailable())
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi enable")
    }

    @Test
    fun checkInternet() {
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi enable")
        Thread.sleep(5000)
        assertTrue(Utils.isInternetAvailable())
        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
    }

}