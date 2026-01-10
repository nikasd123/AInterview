package com.owl.ainterview.ui.nav

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Setup : Screen("setup")
    data object Session : Screen("session")
    data object Report : Screen("report")
    data object Settings : Screen("settings")
}