package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projet_9_OC_RealEstateManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    Scaffold {
        // Screen content
        TopAppBar(
            title = {
                Text(text = "Real Estate Manager")
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Menu,"")
                }
            },
            actions = {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Localized description")
                }// RowScope here, so these icons will be placed horizontally
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Localized description")
                }
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Filled.Search, contentDescription = "Localized description")
                }
            },
            backgroundColor = MaterialTheme.colors.background

        )
        /* ... */
    }

}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Projet_9_OC_RealEstateManagerTheme {
        Greeting()
    }
}