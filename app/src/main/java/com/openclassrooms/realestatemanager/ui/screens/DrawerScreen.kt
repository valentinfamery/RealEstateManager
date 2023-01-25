package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
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


    val user by userViewModel.userData.collectAsState()


    ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
        val (username,userEmail,buttonLogout,item1,item2) = createRefs()


                Text(text = user?.email.toString(), modifier = Modifier.constrainAs(username) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                })

                Text(
                    text = user?.username.toString(),
                    modifier = Modifier.constrainAs(userEmail) {
                        top.linkTo(username.bottom, margin = 5.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    })










            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                label = { Text("Settings") },
                selected = true,
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("settingsScreen") {
                    }
                },
                modifier = Modifier
                    .padding(
                        NavigationDrawerItemDefaults.ItemPadding
                    )
                    .constrainAs(item1) {
                        top.linkTo(username.bottom, margin = 10.dp)
                    }
            )

            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Info, contentDescription = null) },
                label = { Text("CurrencyConverter") },
                selected = true,
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("currencyConverterScreen") {
                    }
                },
                modifier = Modifier
                    .padding(
                        NavigationDrawerItemDefaults.ItemPadding

                    )
                    .constrainAs(item2) {
                        top.linkTo(item1.bottom, margin = 0.dp)
                    }
            )









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
