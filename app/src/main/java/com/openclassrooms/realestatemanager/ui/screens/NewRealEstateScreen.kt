package com.openclassrooms.realestatemanager.ui.screens

import android.app.Activity
import android.widget.NumberPicker
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme

@ExperimentalMaterial3Api
@Composable
fun NewRealEstateScreen() {
    Scaffold (
        content = {
            var text by rememberSaveable { mutableStateOf("") }
            ConstraintLayout(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxHeight()
            ) {
                // Create references for the composables to constrain

                val (textField1,textField2,textField3,numberPicker,numberPicker2 , numberPicker3,numberPicker4,centerAlignedTopAppBar,confirmAddButton) = createRefs()

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
                    modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }

                )

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Type") },
                    singleLine = true,
                    modifier = Modifier.constrainAs(textField1) {
                        top.linkTo(centerAlignedTopAppBar.bottom, margin = 10.dp)
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

                AndroidView(
                    modifier = Modifier.constrainAs(numberPicker2){
                        top.linkTo(numberPicker.bottom, margin = 25.dp)
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

                AndroidView(
                    modifier = Modifier.constrainAs(numberPicker3){
                        top.linkTo(numberPicker2.bottom, margin = 25.dp)
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

                AndroidView(
                    modifier = Modifier.constrainAs(numberPicker4){
                        top.linkTo(numberPicker3.bottom, margin = 25.dp)
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

                Button(
                    onClick = { /* Do something! */ },
                    modifier = Modifier.constrainAs(confirmAddButton) {
                        top.linkTo(numberPicker4.bottom, margin = 25.dp)
                        bottom.linkTo(parent.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    },
                ) {
                    Text("Confirm")
                }



            }











        },

        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Projet_9_OC_RealEstateManagerTheme{
        NewRealEstateScreen()
    }
}