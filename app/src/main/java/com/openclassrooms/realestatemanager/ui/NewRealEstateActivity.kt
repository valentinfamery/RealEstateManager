package com.openclassrooms.realestatemanager.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme

class NewRealEstateActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projet_9_OC_RealEstateManagerTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}
@ExperimentalMaterial3Api
@Composable
fun Greeting(name: String) {
    Scaffold (
        content = {
            Text(text = "ccccccccccccccc")
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "New Estate Manager")
                },
                navigationIcon = {
                    val activity = (LocalContext.current as? Activity)
                    IconButton(onClick = {
                        activity?.finish()
                    }) {
                        Icon(Icons.Filled.ArrowBack,"")
                    }
                },

            )
        }
            )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Projet_9_OC_RealEstateManagerTheme() {
        Greeting("Android")
    }
}