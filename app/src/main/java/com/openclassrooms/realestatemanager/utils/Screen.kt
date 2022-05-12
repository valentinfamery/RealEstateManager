package com.openclassrooms.realestatemanager.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object ListScreen :Screen("listScreen","List",Icons.Default.List)
    object MapScreen : Screen("mapScreen","Map",Icons.Default.LocationOn)
}
