package com.example.camperpro.android.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.NavGraphs
import com.example.camperpro.android.composables.AppScaffold
import com.example.camperpro.android.composables.BottomBar
import com.ramcosta.composedestinations.DestinationsNavHost

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {

    val sheetState = LocalDependencyContainer.current.appViewModel.bottomSheetIsShowing

    AppScaffold(
        navController = navController,
        sheetState = sheetState,
        bottomBar = { BottomBar(navController = navController) },
        startRoute = NavGraphs.root.startRoute,
        topBar = false
    ) {
        DestinationsNavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            navGraph = NavGraphs.root,
            startRoute = NavGraphs.root.startRoute
        )
    }

}