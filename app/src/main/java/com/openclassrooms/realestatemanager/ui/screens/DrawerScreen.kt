package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel
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
        val (userProfilePicture,username,userEmail,drawerItems,buttonLogout) = createRefs()
        val userDataState = userViewModel.userData.observeAsState()

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiary)
                .constrainAs(userProfilePicture) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        )


        Text(text = userDataState.value?.username ?:"",modifier = Modifier.constrainAs(username) {
            top.linkTo(userProfilePicture.bottom, margin = 15.dp)
            start.linkTo(parent.start, margin = 0.dp)
            end.linkTo(parent.end, margin = 0.dp)
        })
        Text(text = userDataState.value?.email ?:"",modifier = Modifier.constrainAs(userEmail) {
            top.linkTo(username.bottom, margin = 5.dp)
            start.linkTo(parent.start, margin = 0.dp)
            end.linkTo(parent.end, margin = 0.dp)
        })

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
                        top.linkTo(userEmail.bottom, margin = 15.dp)
                    }
            )
        }



        Button(
            onClick = {

                auth.signOut()
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