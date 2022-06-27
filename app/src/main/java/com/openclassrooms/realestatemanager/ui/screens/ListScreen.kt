package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(drawerState: DrawerState, scope: CoroutineScope, realEstateViewModel: RealEstateViewModel, innerPadding: PaddingValues, ){
    val context = LocalContext.current
    val items: List<RealEstate> by realEstateViewModel.getRealEstates.observeAsState(listOf())

    var expanded by remember { mutableStateOf(false) }






    Scaffold(
        modifier = Modifier.padding(innerPadding),
        topBar = {

                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "RealEstateManager")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu,"")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            expanded = !expanded
                        }) {
                            Icon(Icons.Filled.Search, contentDescription = "")
                        }
                    },
                )

        },
        content = {

            DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    DropdownMenuItem(
                        text = { Text("200-300 m2") },
                        onClick = { /* Handle settings! */ },
                        trailingIcon = {
                            val checkedState = remember { mutableStateOf(true) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                        })

                    DropdownMenuItem(
                        text = { Text("Ecoles") },
                        onClick = { /* Handle settings! */ },
                        trailingIcon = {
                            val checkedState = remember { mutableStateOf(true) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                        })
                    DropdownMenuItem(
                        text = { Text("Commerces") },
                        onClick = { /* Handle settings! */ },
                        trailingIcon = {
                            val checkedState = remember { mutableStateOf(true) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                        })
                    DropdownMenuItem(
                        text = { Text("derniere semaine") },
                        onClick = { /* Handle settings! */ },
                        trailingIcon = {
                            val checkedState = remember { mutableStateOf(true) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                        })
                    DropdownMenuItem(
                        text = { Text("vendu 3 derniers mois") },
                        onClick = { /* Handle settings! */ },
                        trailingIcon = {
                            val checkedState = remember { mutableStateOf(true) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                        })
                    DropdownMenuItem(
                        text = { Text("long island") },
                        onClick = { /* Handle settings! */ },
                        trailingIcon = {
                            val checkedState = remember { mutableStateOf(true) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                        })
                    DropdownMenuItem(
                        text = { Text("min 3 photos") },
                        onClick = { /* Handle settings! */ },
                        trailingIcon = {
                            val checkedState = remember { mutableStateOf(true) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                        })
                    DropdownMenuItem(
                        text = { Text(" \$1,500,000 et \$2,000,000") },
                        onClick = { /* Handle settings! */ },
                        trailingIcon = {
                            val checkedState = remember { mutableStateOf(true) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                        })
                    Button(onClick = {expanded = false}) {
                        Text(text = "Filter")
                    }
                }
            }


            LazyColumn(modifier = Modifier.padding(it),
            ){
                items(items){ item ->
                    RowList(context,item,realEstateViewModel)
                }
            }
        }
    )
}




