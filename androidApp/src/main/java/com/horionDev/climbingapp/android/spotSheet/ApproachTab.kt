package com.horionDev.climbingapp.android.spotSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
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
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.composables.SmallAppButton
import com.horionDev.climbingapp.domain.model.composition.AppMarker
import com.horionDev.climbingapp.domain.model.entities.Crag

@Composable
fun ApproachTab(crag: Crag) {

    val access = listOf(
        ParkingSpot(
            "Parking Lot, Ceuse Climbing",
            "\"this is the handiest access, parking lot 1 which is situated on the right just before the cottage “La Grange aux loup”. From there take an easy footpath on the left, the path is marked in yellow. Cross the forest road to reach the “chemin de ronde”.\\n\" +\n" +
                    "            \"From there follow the chemin de ronde taking right or left depending to which area you want to go and reach directly the different sectors taking well marked paths.\"",
            44.484945, 5.937846
        ),
        ParkingSpot(
            "Parking Grande Face",
            "This is a direct access for \"Grande face\".\n" +
                    "To reach the parking lot 3, take right just before Les Guerins campsite toward Les Bonnets and follow the track on the left during 0.6 miles (1km). Park your car in the big hairpin turn just before the gate at the level of the ford. Take a very steep path that crosses a forest road, then a footpath to join the “Chemin de ronde” under K-Rock’n’roll sector at “Grande Face”.\n" +
                    "Very steep footpath, where you will have to control your effort… But very quick, especially to go down.",
            44.4943205,
            5.9541723
        ),
        ParkingSpot(
            "Parking Col des Guerin",
            "Take a steep footpath at the back right of the Col des Guerins parking lot 2. This is a very steep path which cross the forest road and join the access 1 in a hairpin turn.",
            44.4876363,
            5.9360154
        )
    )

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Access",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        ParkingSpotsMap(access)
        ParkingDescription(access)
    }
}

data class ParkingSpot(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)

@Composable
fun ParkingSpotsMap(access: List<ParkingSpot>) {

    var markers = remember {
        mutableStateListOf<AppMarker>()
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true, mapType = MapType.SATELLITE))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                44.499147, 5.953234
            ), 12f
        )
    }

    LaunchedEffect(Unit) {
        val markerss = mutableListOf<AppMarker>()
        access.forEach {
            markerss.add(
                AppMarker(
                    placeLinkedId = "parking",
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            )
        }
        markerss.add(
            AppMarker(
                placeLinkedId = "crag",
                latitude = 44.499147, longitude = 5.953234
            )
        )
        markers.addAll(markerss)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
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
fun ParkingDescription(access: List<ParkingSpot>) {
    access.forEach {
        ParkingSpotRow(it)
    }
}

@Composable
fun ParkingSpotRow(parkingSpot: ParkingSpot) {
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

