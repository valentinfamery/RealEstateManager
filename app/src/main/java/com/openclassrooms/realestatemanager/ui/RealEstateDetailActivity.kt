package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.openclassrooms.realestatemanager.ui.screens.RealEstateDetailScreen
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class RealEstateDetail : ComponentActivity() {



    private val realEstateViewModel: RealEstateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val itemId = intent.getStringExtra("itemId")


        setContent {
            Projet_9_OC_RealEstateManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RealEstateDetailScreen(realEstateViewModel,itemId)
                }
            }
        }
    }
}
