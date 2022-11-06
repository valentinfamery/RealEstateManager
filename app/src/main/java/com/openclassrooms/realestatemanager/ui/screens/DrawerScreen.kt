package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    auth: FirebaseAuth,
    userViewModel: UserViewModel

) {
    val items = listOf(Icons.Default.Settings)
    val selectedItem = remember { mutableStateOf(items[0]) }
    ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
        val (username,userEmail,drawerItems,buttonLogout) = createRefs()



        when(val response = userViewModel.userDataResponse){
            is Response.Success ->{
                val user = response.data

                    Text(text = user?.email.toString(),modifier = Modifier.constrainAs(username) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp) })

                    Text(text = user?.username.toString(),modifier = Modifier.constrainAs(userEmail) {
                        top.linkTo(username.bottom, margin = 5.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    })




            }
            else ->{}
        }

        items.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item, contentDescription = null) },
                label = { Text("Settings") },
                selected = item == selectedItem.value,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = item
                    navController.navigate("settingsScreen") {
                    }
                },
                modifier = Modifier
                    .padding(
                        NavigationDrawerItemDefaults.ItemPadding

                    )
                    .constrainAs(drawerItems) {
                        bottom.linkTo(buttonLogout.top, margin = 100.dp)
                    }
            )
        }



        Button(
            onClick = {

                auth.signOut()
                userViewModel.userDataResponse = Response.Empty
                navController.navigate("signInScreen")
            },
            modifier = Modifier.constrainAs(buttonLogout) {
                bottom.linkTo(parent.bottom, margin = 20.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            },
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Localized description",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Logout")
        }

    }
}