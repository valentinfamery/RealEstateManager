package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import com.openclassrooms.realestatemanager.utils.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
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
            Divider(modifier = Modifier
                .fillMaxHeight(0.001f)
                .fillMaxWidth())
            NavigationDrawerItem(
                icon = { Icon(Screen.SettingsScreen.icon, contentDescription = null) },
                label = { Text(Screen.SettingsScreen.title) },
                selected = false,
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.SettingsScreen.route) {
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
                label = { Text(Screen.CurrencyConverterScreen.title) },
                selected = false,
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.CurrencyConverterScreen.route) {
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
                    navController.navigate(Screen.SignInScreen.route)
                }
            ) {
                Text(stringResource(R.string.buttonLogout))
            }
        }

}
