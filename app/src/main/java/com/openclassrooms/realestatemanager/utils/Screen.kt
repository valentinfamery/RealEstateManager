package com.openclassrooms.realestatemanager.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object ListScreen :Screen("listScreen","List",Icons.Default.List)
    object MapScreen : Screen("mapScreen","Map",Icons.Default.LocationOn)
    object MainScreen : Screen("mainScreen","",Icons.Default.Home)
    object SettingsScreen : Screen("settingsScreen","Settings",Icons.Default.Settings)
    object RegisterScreen : Screen("registerScreen","Register",Icons.Default.AccountCircle)
    object SignInScreen : Screen("signInScreen","Login",Icons.Default.AccountCircle)
    object NewScreen : Screen("newScreen","New Real Estate",Icons.Default.Add)
    object CurrencyConverterScreen : Screen("currencyConverterScreen","CurrencyConverter", Icons.Default.ShoppingCart)
    object DetailScreen : Screen("detailScreen","RealEstate",Icons.Default.Info)
    object EditScreen : Screen("editScreen","Edit Real Estate",Icons.Default.Edit)
}
