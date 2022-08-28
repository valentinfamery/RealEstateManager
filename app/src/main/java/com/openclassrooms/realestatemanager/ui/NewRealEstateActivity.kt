package com.openclassrooms.realestatemanager.ui

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.realestatemanager.di.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.ui.screens.NewRealEstateScreen
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class NewRealEstateActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Projet_9_OC_RealEstateManagerTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModelFactory = RealEstateViewModelFactory(
                        LocalContext.current.applicationContext as Application,
                        lifecycleScope
                    )

                    val owner = LocalViewModelStoreOwner.current

                    owner?.let {
                        val realEstateViewModel: RealEstateViewModel = viewModel(
                            it,
                            "MainViewModel",
                            viewModelFactory
                        )

                        NewRealEstateScreen(realEstateViewModel)
                    }
                }
            }
        }
    }
}



