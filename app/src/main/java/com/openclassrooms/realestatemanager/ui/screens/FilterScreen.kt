package com.openclassrooms.realestatemanager.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.FilterResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen() {
    val listType = listOf("Appartement", "Loft", "Manoir", "Maison")
    val activity = LocalContext.current as Activity
    var expanded by remember { mutableStateOf(false) }

    var entryType by rememberSaveable { mutableStateOf("") }
    var entryCity by rememberSaveable { mutableStateOf("") }
    var entryMinSurface by rememberSaveable { mutableStateOf("") }
    var entryMaxSurface by rememberSaveable { mutableStateOf("") }
    var entryMinPrice by rememberSaveable { mutableStateOf("") }
    var entryMaxPrice by rememberSaveable { mutableStateOf("") }
    var onTheMarketLessALastWeek by rememberSaveable{ mutableStateOf(false)}
    var soldOn3LastMonth by rememberSaveable{ mutableStateOf(false)}
    var min3photos by rememberSaveable{ mutableStateOf(false)}
    var schools by rememberSaveable{ mutableStateOf(false)}
    var shops by rememberSaveable{ mutableStateOf(false)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

ConstraintLayout(modifier = Modifier.fillMaxSize()) {

    val (buttonClose,buttonReset,column,buttonFilter) = createRefs()

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

        TextField(
            value = entryType,
            onValueChange = { entryType = it },
            label = { Text("Type") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listType.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            entryType = it
                            expanded = false
                            /* Handle edit! */
                        },
                    )
                }
            }

        TextField(
            value = entryCity,
            onValueChange = { entryCity = it },
            label = { Text("City") },
            singleLine = true
        )

        Row() {
            TextField(
                value = entryMinSurface,
                onValueChange = { entryMinSurface = it },
                label = { Text("MinSurface") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.4f)
            )
            TextField(
                value = entryMaxSurface,
                onValueChange = { entryMaxSurface = it },
                label = { Text("MaxSurface") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.4f)
            )
        }

        Row() {
            TextField(
                value = entryMinPrice,
                onValueChange = { entryMinPrice = it },
                label = { Text("MinPrice") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.4f)
            )
            TextField(
                value = entryMaxPrice,
                onValueChange = { entryMaxPrice = it },
                label = { Text("MaxPrice") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.4f)
            )
        }

        Text(text = "onTheMarketLessALastWeek")
        Checkbox(checked = onTheMarketLessALastWeek, onCheckedChange ={onTheMarketLessALastWeek = it})
        Text(text = "soldOn3LastMonth")
        Checkbox(checked = soldOn3LastMonth, onCheckedChange ={soldOn3LastMonth = it})
        Text(text = "min3photos")
        Checkbox(checked = min3photos, onCheckedChange ={min3photos = it})
        Text(text = "schools")
        Checkbox(checked = schools, onCheckedChange ={schools = it})
        Text(text = "shops")
        Checkbox(checked = shops, onCheckedChange ={shops = it})
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

    Button(
        onClick = {
        var resultFilter = FilterResult(
            entryType,
            entryCity,
            entryMinSurface,
            entryMaxSurface,
            entryMinPrice,
            entryMaxPrice,
            onTheMarketLessALastWeek,
            soldOn3LastMonth,
            min3photos,
            schools,
            shops
        )
        val intent = Intent()
        intent.putExtra("resultFilter",resultFilter)
        activity.setResult(0,intent)
            activity.finish()
    },
        modifier = Modifier.constrainAs(buttonFilter){
            bottom.linkTo(parent.bottom, margin = 10.dp)
        }

    ) {

    }

}
}