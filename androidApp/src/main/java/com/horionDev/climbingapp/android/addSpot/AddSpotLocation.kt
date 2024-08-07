package com.horionDev.climbingapp.android.addSpot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.extensions.hasLocationPermission
import com.horionDev.climbingapp.android.ui.theme.Dimensions
import com.horionDev.climbingapp.managers.location.LocationManager
import com.horionDev.climbingapp.utils.Constants
import com.horionDev.climbingapp.R
import com.horionDev.climbingapp.android.destinations.AddSpotInformationsScreenDestination
import com.horionDev.climbingapp.android.destinations.AddSpotLocationScreenDestination
import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.utils.Globals
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddSpotLocationScreen(navigator: DestinationsNavigator) {

    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = if (context.hasLocationPermission) {
            CameraPosition.fromLatLngZoom(
                LatLng(
                    Globals.GeoLoc.lastKnownLocation.latitude,
                    Globals.GeoLoc.lastKnownLocation.longitude
                ), 10f
            )
        } else {
            CameraPosition.fromLatLngZoom(
                LatLng(
                    Constants.DEFAULT_LOCATION.latitude, Constants.DEFAULT_LOCATION.longitude
                ), 10f
            )
        }
    }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Create a spot",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(modifier = Modifier.alpha(0f), onClick = { }) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
            }
        }

        Text(modifier = Modifier.padding(horizontal = 18.dp), text = "Choose location", fontWeight = FontWeight.Bold)

        Divider(modifier = Modifier.padding(vertical = 10.dp).padding(horizontal = 18.dp))

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false, myLocationButtonEnabled = false
                ),
                cameraPositionState = cameraPositionState,
            ) {
            }

            if (cameraPositionState.position.zoom > 10f) {
                Image(painter = painterResource(id = com.horionDev.climbingapp.android.R.drawable.pins), contentDescription = "")
            } else {
                Image(
                    painter = painterResource(id = com.horionDev.climbingapp.android.R.drawable.pins),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(
                        androidx.compose.ui.graphics.Color.Red
                    )
                )
            }
        }
            AppButton(
                isActive = cameraPositionState.position.zoom > 10f,
                onClick = {
                    val lat = cameraPositionState.position.target.latitude
                    val long = cameraPositionState.position.target.longitude
                    navigator.navigate(AddSpotInformationsScreenDestination(crag = Crag(latitude = lat, longitude = long)))
                },
                modifier = Modifier.padding(18.dp),
                textRes = R.string.next
            )
    }
}