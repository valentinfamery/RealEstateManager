package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.openclassrooms.realestatemanager.R

@Composable
fun FilterScreen(navController: NavHostController) {

    var sliderPositionPrice by rememberSaveable { mutableStateOf(0f) }
    var filterType by rememberSaveable { mutableStateOf("") }
    var filterTypeState by rememberSaveable { mutableStateOf(false) }

    val listType = listOf("Appartement", "Loft", "Manoir", "Maison")

    val listNumberRoom = listOf("1", "2", "3", "4","5","6","7","8")

ConstraintLayout(modifier = Modifier.fillMaxSize()) {

    val (buttonClose, buttonReset, textPropertyType, textPropertyNumberRoom, textPropertyPrice, flowRowType, flowRowNumberRoom,sliderPrice) = createRefs()

    Button(
        onClick = { /* Do something! */ },
        modifier = Modifier.constrainAs(buttonReset) {
            top.linkTo(parent.top, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
        }
    ) {
        Text("Reset")
    }



    IconButton(
        onClick = {
                  navController.popBackStack()
        },
        modifier = Modifier.constrainAs(buttonClose) {
            top.linkTo(parent.top, margin = 10.dp)
            start.linkTo(parent.start, margin = 10.dp)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_close_24),
            contentDescription = ""
        )
    }

    Text(text = "Property Type", modifier = Modifier.constrainAs(textPropertyType) {
        top.linkTo(parent.top, margin = 75.dp)
        start.linkTo(parent.start, margin = 10.dp)
    })

    Row(modifier = Modifier.horizontalScroll(rememberScrollState()).constrainAs(flowRowType) {
        top.linkTo(textPropertyType.bottom, margin = 10.dp)
        start.linkTo(parent.start, margin = 10.dp)
    }) {

        FlowRow {
            listType.forEach{
                OutlinedButton(onClick = {
                    filterType = ""
                    filterTypeState = !filterTypeState
                }) { Text(it) }
                Divider(color = Color.Transparent, modifier = Modifier.fillMaxHeight().width(10.dp))
            }
        }
    }

    Text(text = "Property Number Room", modifier = Modifier.constrainAs(textPropertyNumberRoom) {
        top.linkTo(flowRowType.bottom, margin = 10.dp)
        start.linkTo(parent.start, margin = 10.dp)
    })

    Row(modifier = Modifier.horizontalScroll(rememberScrollState()).constrainAs(flowRowNumberRoom) {
        top.linkTo(textPropertyNumberRoom.bottom, margin = 10.dp)
        start.linkTo(parent.start, margin = 10.dp)
    }) {

        FlowRow {
            listNumberRoom.forEach {
                OutlinedButton(onClick = {  }) { Text(it) }
                Divider(color = Color.Transparent, modifier = Modifier.fillMaxHeight().width(10.dp))
            }
        }
    }

    Text(text = "Property Price", modifier = Modifier.constrainAs(textPropertyPrice) {
        top.linkTo(flowRowNumberRoom.bottom, margin = 10.dp)
        start.linkTo(parent.start, margin = 10.dp)
    })


    Row(modifier = Modifier.constrainAs(sliderPrice) {
        top.linkTo(textPropertyPrice.bottom, margin = 10.dp)
        start.linkTo(parent.start, margin = 10.dp)
        end.linkTo(parent.end, margin = 10.dp)
    }) {
        Column {
            Text(text = sliderPositionPrice.toString())
            Slider(value = sliderPositionPrice,valueRange = 0f..100f, onValueChange = { sliderPositionPrice = it })
        }
    }
}
}