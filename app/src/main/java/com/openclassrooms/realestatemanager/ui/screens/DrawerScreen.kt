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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
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
    userViewModel: UserViewModel,
    modifier: Modifier
) {

    val user by userViewModel.userData.collectAsState()



        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxSize(0.025f))
            Text(text = user?.email.toString())
            Spacer(modifier = Modifier.fillMaxSize(0.025f))
            Text(text = user?.username.toString())
            Spacer(modifier = Modifier.fillMaxSize(0.025f))
            Divider(modifier = Modifier.fillMaxHeight(0.001f).fillMaxWidth())
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                label = { Text("Settings") },
                selected = false,
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("settingsScreen") {
                    }
                },
                modifier = Modifier
                    .padding(
                        NavigationDrawerItemDefaults.ItemPadding
                    )
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_currency_exchange_24),
                        contentDescription = null
                    )
                },
                label = { Text("CurrencyConverter") },
                selected = false,
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("currencyConverterScreen") {
                    }
                },
                modifier = Modifier
                    .padding(
                        NavigationDrawerItemDefaults.ItemPadding
                    )

            )

            Spacer(modifier = Modifier.fillMaxHeight(0.025f))

            OutlinedButton(
                onClick = {
                    auth.signOut()
                    navController.navigate("signInScreen")
                }
            ) {
                Text("Logout")
            }
        }

}
