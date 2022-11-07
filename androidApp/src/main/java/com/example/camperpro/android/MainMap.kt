package com.example.camperproglobal.android

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.camperpro.android.MyApplicationTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.jetbrains.kmm.shared.data.datasources.remote.HttpRoutes
import com.jetbrains.kmm.shared.domain.model.Location


@Composable
fun MainMap(viewModel: MainMapViewModel = hiltViewModel()) {
    val singapore = LatLng(1.35, 103.87)
    val state by viewModel.state.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    Box() {
        GoogleMap(
            modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState
        ) {
            state.spots.forEach {
                Marker(
                    state = MarkerState(position = LatLng(it.lat, it.long)),
                    title = it.name,
                    snippet = it.name
                )
            }
        }

        FloatingActionButton(onClick = {
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

    }

}

@Preview
@Composable
fun MainMapPreview() {
    MyApplicationTheme {
        MainMap()
    }
}