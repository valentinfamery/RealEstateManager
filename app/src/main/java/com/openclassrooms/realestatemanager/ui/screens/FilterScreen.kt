package com.openclassrooms.realestatemanager.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.R

@Composable
fun FilterScreen() {

    var areaAppartementFilter200300 by remember { mutableStateOf(false) }
    var schoolsFilter by remember { mutableStateOf(false) }
    var shopsFilter  by remember { mutableStateOf(false) }
    var lastWeekFilterForSale  by remember { mutableStateOf(false) }
    var last3MonthsHouseFilterSold  by remember { mutableStateOf(false) }
    var longIslandFilter by remember { mutableStateOf(false) }
    var min3Photos by remember { mutableStateOf(false) }
    var priceFilter1500000200000 by remember { mutableStateOf(false) }

    val activity = LocalContext.current as Activity

ConstraintLayout(modifier = Modifier.fillMaxSize()) {

    val (buttonClose,buttonReset,column) = createRefs()

    Button(
        onClick = {
            activity.finish()
                  },
        modifier = Modifier.constrainAs(buttonReset) {
            top.linkTo(parent.top, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
        }
    ) {
        Text("Reset")
    }

    Column(modifier = Modifier.constrainAs(column) {
        top.linkTo(parent.top, margin = 50.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }) {


        Switch(
            checked = areaAppartementFilter200300,
            onCheckedChange = { areaAppartementFilter200300 = it },
        )

        Switch(
            checked = schoolsFilter,
            onCheckedChange = { schoolsFilter = it })

        Switch(
            checked = shopsFilter,
            onCheckedChange = { shopsFilter = it })

        Switch(
            checked = lastWeekFilterForSale,
            onCheckedChange = { lastWeekFilterForSale = it })

        Switch(
            checked = last3MonthsHouseFilterSold,
            onCheckedChange = { last3MonthsHouseFilterSold = it })

        Switch(
            checked = longIslandFilter,
            onCheckedChange = { longIslandFilter = it })

        Switch(
            checked = min3Photos,
            onCheckedChange = { min3Photos = it })

        Switch(
            checked = priceFilter1500000200000,
            onCheckedChange = { priceFilter1500000200000 = it })

    }



    IconButton(
        onClick = {
                  activity.finish()
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

    Button(onClick = {
        val intent = Intent()
        activity.setResult(0,intent)
    }) {

    }

}
}