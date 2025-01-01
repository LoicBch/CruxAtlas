package com.horionDev.climbingapp.android.areaDetails

import CragDetailsDto
import androidx.compose.runtime.Composable
import com.horionDev.climbingapp.domain.model.entities.Area
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun InfosTab(
    area: Area,
    navigator: DestinationsNavigator,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onNavigateClick: () -> Unit,
    onView3DClick: () -> Unit
) {


}