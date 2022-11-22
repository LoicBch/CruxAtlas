package com.example.camperpro.android.components

sealed class Screen(val route: String) {
    object MainMapScreen : Screen("main_map_screen")
    object DetailsScreen : Screen("detail_screen")
}