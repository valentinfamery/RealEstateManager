package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.ui.components.TopBar
import com.openclassrooms.realestatemanager.utils.Screen
import com.openclassrooms.realestatemanager.utils.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(navigateToBack : () -> Unit){

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {


        TopBar(
            title = Screen.CurrencyConverterScreen.title,
            backNavigate = true,
            filterScreen = false,
            drawerButton = false,
            navigateToFilterScreen = { /*TODO*/ },
            navigateToBack = {navigateToBack()},
            openDrawer = { /*TODO*/ },
            modifier = Modifier
        )

        var euro by remember { mutableStateOf("") }
        var dollar by remember { mutableStateOf("") }

        OutlinedTextField(
            value = euro,
            onValueChange = {
                euro = it
                val result = it.toIntOrNull()
                dollar = if (result != null)
                    Utils.convertEuroToDollar(result).toString()
                else
                    "Incorrect Euro Value"
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = { Text("Euro") },
            modifier = Modifier
                .onFocusChanged {
                    if (it.isFocused)
                        euro = ""
                }
                .padding(8.dp)
        )
        OutlinedTextField(
            value = dollar,
            onValueChange = {
                dollar = it
                val result = it.toIntOrNull()
                euro = if (result != null)
                    Utils.convertDollarToEuro(result).toString()
                else
                    "Incorrect Dollar Value"
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = { Text("Dollar") },
            modifier = Modifier
                .onFocusChanged {
                    if (it.isFocused)
                        dollar = ""
                }
                .padding(8.dp)
        )
    }
}