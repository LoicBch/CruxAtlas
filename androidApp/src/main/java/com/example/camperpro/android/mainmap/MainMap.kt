package com.example.camperpro.android.mainmap

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.camperpro.android.MyApplicationTheme
import com.example.camperpro.android.components.Lotties.LoaderSpots
import com.example.camperpro.android.destinations.SpotSheetDestination
import com.example.camperpro.android.ui.theme.SemiClearGrey
import com.example.camperpro.android.ui.theme.SkyBlue
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.jetbrains.kmm.shared.domain.model.Location
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun MainMap(navigator: DestinationsNavigator, viewModel: MainMapViewModel = getViewModel()) {
    val singapore = LatLng(45.7, 4.8)
    val state by viewModel.state.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    val imageAlpha: Float by animateFloatAsState(
        targetValue = if (state.loading) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        )
    )

    Text(text = "test")

    Box() {
        GoogleMap(
            modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState
        ) {
            state.spots.forEach { spot ->
                Marker(
                    state = MarkerState(position = LatLng(spot.lat, spot.long)),
                    title = spot.name,
                    snippet = spot.name,
                    onClick = {
//                        navController.navigate(Screen.DetailsScreen.route + "/5")
                        navigator.navigate(SpotSheetDestination(spot))
                        true
                    }
                )
            }
        }

        FloatingActionButton(backgroundColor = SkyBlue, onClick = {
            viewModel.getSpotAroundPos(
                Location(
                    cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude
                )
            )
        }, modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(15.dp), content = {
            Icon(Icons.Filled.Refresh, "Refresh")
        })

        Box(
            modifier = Modifier
                .alpha(imageAlpha)
                .fillMaxSize()
                .background(SemiClearGrey), contentAlignment = Alignment.Center
        ) {
            LoaderSpots()
        }

    }
}

@Preview
@Composable
fun MainMapPreview() {
    MyApplicationTheme {
    }
}