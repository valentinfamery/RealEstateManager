package com.openclassrooms.realestatemanager.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    openDialogFilter: Boolean,
    editFilterProperty : (filterState : Boolean,entryType : String,entryCity : String,entryMinSurface : Int,entryMaxSurface : Int,
                          entryMinPrice : Int,entryMaxPrice : Int,onTheMarketLessALastWeek : Boolean,soldOn3LastMonth : Boolean,
                          min3photos : Boolean,schools : Boolean,shops : Boolean
    ) -> Unit,
    closeDialog : () -> Unit,
    resetFilter : () -> Unit
) {
    if(openDialogFilter) {

        Dialog(
            onDismissRequest = { },
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.wrapContentSize(),
            ) {
            val listType = listOf("Apartment", "Loft", "Mansion", "House")
            var expanded by remember { mutableStateOf(false) }

            var entryType: String by rememberSaveable { mutableStateOf("") }
            var entryCity: String by rememberSaveable { mutableStateOf("") }
            var entryMinSurface by rememberSaveable { mutableStateOf("0") }
            var entryMaxSurface by rememberSaveable { mutableStateOf("0") }
            var entryMinPrice by rememberSaveable { mutableStateOf("0") }
            var entryMaxPrice by rememberSaveable { mutableStateOf("0") }
            var onTheMarketLessALastWeek by rememberSaveable { mutableStateOf(false) }
            var soldOn3LastMonth by rememberSaveable { mutableStateOf(false) }
            var min3photos by rememberSaveable { mutableStateOf(false) }
            var schools by rememberSaveable { mutableStateOf(false) }
            var shops by rememberSaveable { mutableStateOf(false) }

            var filterState by remember { mutableStateOf(false) }

            val icon = if (expanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                val (buttonClose, buttonReset, column, buttonFilter) = createRefs()

                Button(
                    onClick = {
                        resetFilter()
                        closeDialog()
                    },
                    modifier = Modifier.constrainAs(buttonReset) {
                        top.linkTo(parent.top, margin = 10.dp)
                        end.linkTo(parent.end, margin = 10.dp)
                    }
                ) {
                    Text("Reset")
                }

                Column(modifier = Modifier
                    .constrainAs(column) {
                        top.linkTo(buttonReset.bottom, margin = 25.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight(), horizontalAlignment = Alignment.CenterHorizontally) {

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

                    Spacer(modifier = Modifier.size(10.dp))

                    TextField(
                        value = entryCity,
                        onValueChange = { entryCity = it },
                        label = { Text("City") }
                    )

                    Spacer(modifier = Modifier.size(10.dp))


                    TextField(
                        value = entryMinSurface,
                        onValueChange = { entryMinSurface = it },
                        label = { Text("MinSurface") },
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    TextField(
                        value = entryMaxSurface,
                        onValueChange = { entryMaxSurface = it },
                        label = { Text("MaxSurface") },
                    )


                    Spacer(modifier = Modifier.size(10.dp))

                    TextField(
                        value = entryMinPrice,
                        onValueChange = { entryMinPrice = it },
                        label = { Text("MinPrice") },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    TextField(
                        value = entryMaxPrice,
                        onValueChange = { entryMaxPrice = it },
                        label = { Text("MaxPrice") },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )


                    Spacer(modifier = Modifier.size(10.dp))

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(text = "The property was put on the market less than a week ago")
                        Checkbox(
                            checked = onTheMarketLessALastWeek,
                            onCheckedChange = { onTheMarketLessALastWeek = it })
                        Text(text = "The property has been sold in the last three months")
                        Checkbox(
                            checked = soldOn3LastMonth,
                            onCheckedChange = { soldOn3LastMonth = it })
                        Text(text = "The property has at least three photos")
                        Checkbox(checked = min3photos, onCheckedChange = { min3photos = it })
                        Text(text = "The property has schools nearby")
                        Checkbox(checked = schools, onCheckedChange = { schools = it })
                        Text(text = "The property has shops nearby")
                        Checkbox(checked = shops, onCheckedChange = { shops = it })
                    }


                    Spacer(modifier = Modifier.size(10.dp))
                    Button(
                        onClick = {

                            filterState = true

                            editFilterProperty(
                                filterState,
                                entryType,
                                entryCity,
                                entryMinSurface.toInt(),
                                entryMaxSurface.toInt(),
                                entryMinPrice.toInt(),
                                entryMaxPrice.toInt(),
                                onTheMarketLessALastWeek,
                                soldOn3LastMonth,
                                min3photos,
                                schools,
                                shops
                            )
                            closeDialog()
                        },
                    ) {
                        Text("Filter")
                    }
                    Spacer(modifier = Modifier.size(50.dp))
                }



                IconButton(
                    onClick = {
                        closeDialog()
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


            }
        }
        }
    }
}