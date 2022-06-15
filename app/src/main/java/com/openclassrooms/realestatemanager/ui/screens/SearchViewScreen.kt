package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SearchView(textState: MutableState<TextFieldValue>, navController: NavHostController) {
    TextField(
        value = textState.value,
        onValueChange = { value ->
            textState.value = value
        },
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ,
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            IconButton(
                onClick = {
                    navController.navigate("topBarMap")
                    textState.value = TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                }
            ){
                Icon(
                    Icons.Default.Close,
                    contentDescription = "",
                    modifier = Modifier.size(25.dp)
                )
            }

        },
        trailingIcon = {
            IconButton(
                onClick = {

                    textState.value = TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                }
            ){
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier.size(25.dp)
                )
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
    )
}