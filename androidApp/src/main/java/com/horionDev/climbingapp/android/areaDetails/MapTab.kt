package com.horionDev.climbingapp.android.areaDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.cragsDetails.ParkingSpotsMap
import com.horionDev.climbingapp.android.destinations.CragDetailsDestination
import com.horionDev.climbingapp.android.parcelable.toParcelable
import com.horionDev.climbingapp.domain.model.composition.AppMarker
import com.horionDev.climbingapp.domain.model.composition.calculateCentroid
import com.horionDev.climbingapp.domain.model.entities.Area
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun MapTab(
    area: Area,
    crags: List<Crag>,
    navigator: DestinationsNavigator
) {

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

        ParkingSpotsMap(crags, area, navigator)
//        ParkingDescription(crag.sectors.flatMap { it.parkingSpots!! })
    }
}

@Composable
fun ParkingSpotsMap(crags: List<Crag>, area: Area, navigator: DestinationsNavigator) {
    var markers = remember {
        mutableStateListOf<AppMarker>()
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true, mapType = MapType.SATELLITE))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                area.polygon.calculateCentroid().latitude,
                area.polygon.calculateCentroid().longitude
            ), 10f
        )
    }

    LaunchedEffect(Unit) {
        val markerss = mutableListOf<AppMarker>()
        crags.forEach { crag ->
            markerss.add(
                AppMarker(
                    placeLinkedId = crag.id.toString(),
                    latitude = crag.latitude, longitude = crag.longitude
                )
            )
        }
        markers.addAll(markerss)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        properties = mapProperties,
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = false,
        ),
        cameraPositionState = cameraPositionState
    ) {
        markers.forEach { marker ->
            val markerState = MarkerState(position = LatLng(marker.latitude, marker.longitude))
            Marker(
                icon = BitmapDescriptorFactory.fromResource(R.drawable.marker),
                state = markerState,
                onClick = {
                    navigator.navigate(
                        CragDetailsDestination(
                            crags.find { it.id == marker.placeLinkedId.toInt() }!!.toParcelable()
                        )
                    )
                    true
                })
        }

        if (area.polygon.isNotEmpty()) {
            Polygon(
                points = area.polygon.map {
                    LatLng(it.latitude, it.longitude)
                },
                strokeColor = Color.Blue, // Couleur des bordures
                fillColor = Color(0x550000FF), // Couleur de remplissage (semi-transparente)
                strokeWidth = 1f
            )
        }
    }
}