package com.openclassrooms.realestatemanager.ui

import android.app.Activity
import android.os.Bundle
import android.widget.NumberPicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme

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
                    Greeting()
                }
            }
        }
    }
}
@ExperimentalMaterial3Api
@Composable
fun Greeting() {

    Scaffold (
        content = {

                var text by rememberSaveable { mutableStateOf("") }

                ConstraintLayout() {
                    // Create references for the composables to constrain
                    val (textField1,textField2,textField3,numberPicker) = createRefs()

                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Type") },
                        singleLine = true,
                        modifier = Modifier.constrainAs(textField1) {
                            top.linkTo(parent.top, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }

                    )

                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Price") },
                        singleLine = true ,
                        modifier = Modifier.constrainAs(textField2){
                            top.linkTo(textField1.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                    )

                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Area") },
                        singleLine = true,
                        modifier = Modifier.constrainAs(textField3){
                            top.linkTo(textField2.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        }
                    )

                    AndroidView(
                        modifier = Modifier.constrainAs(numberPicker){
                            top.linkTo(textField3.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 50.dp)
                            end.linkTo(parent.end, margin = 50.dp)
                        },
                        factory = { context ->
                            NumberPicker(context).apply {
                                setOnValueChangedListener { numberPicker, i, i2 ->  }
                                minValue = 0
                                maxValue = 10
                            }
                        }
                    )

                }











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
    Projet_9_OC_RealEstateManagerTheme{
        Greeting()
    }
}


