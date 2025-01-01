package com.horionDev.climbingapp.android.cragsDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.SmallAppButton
import com.horionDev.climbingapp.android.destinations.AddSpotInformationsScreenDestination
import com.horionDev.climbingapp.android.parcelable.toParcelable
import com.horionDev.climbingapp.data.model.dto.ParkingSpotDto
import com.horionDev.climbingapp.domain.model.composition.AppMarker
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun ApproachTab(crag: Crag, navigator: DestinationsNavigator) {

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 18.dp),
            text = "Access",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        if (crag.sectors.flatMap { it.parkingSpots!! }.isEmpty()) {
            MissingDataBlock(
                R.string.no_infos,
                onAddDataClick = {
                    navigator.navigate(
                        AddSpotInformationsScreenDestination(crag.toParcelable())
                    )
                }, height = 50)
        }

        ParkingSpotsMap(crag)
        ParkingDescription(crag.sectors.flatMap { it.parkingSpots!! })
    }
}

@Composable
fun ParkingSpotsMap(crag: Crag) {
    var markers = remember {
        mutableStateListOf<AppMarker>()
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true, mapType = MapType.SATELLITE))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                crag.latitude, crag.longitude
            ), 18f
        )
    }

    LaunchedEffect(Unit) {
        val markerss = mutableListOf<AppMarker>()
        if (crag.sectors.flatMap { it.parkingSpots!! }.isEmpty()) {
            crag.sectors.flatMap { it.parkingSpots!! }.forEach {
                markerss.add(
                    AppMarker(
                        placeLinkedId = "parking",
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                )
            }
        }
        markerss.add(
            AppMarker(
                placeLinkedId = "crag",
                latitude = crag.latitude, longitude = crag.longitude
            )
        )
        markers.addAll(markerss)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        properties = mapProperties,
        uiSettings = MapUiSettings(
            scrollGesturesEnabled = true,
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
        ),
        cameraPositionState = cameraPositionState
    ) {
        markers.forEach { marker ->
            val markerState = MarkerState(position = LatLng(marker.latitude, marker.longitude))
            if (marker.placeLinkedId == "parking") {
                Marker(
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.pins_p),
                    state = markerState,
                    onClick = {
                        true
                    })
            } else {
                Marker(
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.marker),
                    state = markerState,
                    onClick = {
                        true
                    })
            }
        }
    }
}

@Composable
fun ParkingDescription(access: List<ParkingSpotDto>) {
    access.forEach {
        ParkingSpotRow(it)
    }
}

@Composable
fun ParkingSpotRow(parkingSpot: ParkingSpotDto) {
    Column(modifier = Modifier.padding(vertical = 18.dp)) {
        Text(
            text = parkingSpot.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = parkingSpot.description,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        SmallAppButton(
            isActive = true,
            onClick = { /*TODO*/ },
            modifier = Modifier,
            textRes = R.string.navigate
        )
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}

