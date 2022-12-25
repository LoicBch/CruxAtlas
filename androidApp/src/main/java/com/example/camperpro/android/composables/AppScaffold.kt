package com.example.camperpro.android.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.camperpro.android.appCurrentDestinationAsState
import com.example.camperpro.android.destinations.AndroidAppDestination
import com.example.camperpro.android.startAppDestination
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.spec.Route

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AppScaffold(
    startRoute: Route,
    navController: NavController,
    topBar: Boolean,
    sheetState: ModalBottomSheetState,
    bottomBar: @Composable (AndroidAppDestination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val destination =
        navController.appCurrentDestinationAsState().value ?: startRoute.startAppDestination

    ModalBottomSheetLayout(
        sheetState = sheetState, sheetContent = {
            BottomSheetLayoutScreen()
        }, scrimColor = Color.Black.copy(alpha = 0.32f)
    ) {
        Scaffold(topBar = { }, bottomBar = {
            if (!destination.shouldShowBottomBar) bottomBar(destination)
        }, content = content
        )
    }
}
