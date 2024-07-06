package com.horionDev.climbingapp.android.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppScaffold(
    sheetState: ModalBottomSheetState,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = sheetState, sheetContent = {
            BottomSheetLayoutScreen()
        }, scrimColor = Color.Black.copy(alpha = 0.32f)
    ) {
        Scaffold(
            bottomBar = {
                bottomBar()
            }, content = content
        )
    }
}
