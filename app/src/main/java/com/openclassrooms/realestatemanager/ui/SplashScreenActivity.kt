package com.openclassrooms.realestatemanager.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseApp
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel

class SplashScreenActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        startActivityInTermsOfUserIsConnected()
    }

    private fun startActivityInTermsOfUserIsConnected(){
        userViewModel.isCurrentUserLoggedIn.observe(this) { t: Boolean? ->
            if (t == true) {
                startActivityMain();
            } else {
                startActivityLogin();
            }
            finish();
        }
    }

    private fun startActivityLogin(){
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun startActivityMain(){
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        startActivity(intent)
    }
}


