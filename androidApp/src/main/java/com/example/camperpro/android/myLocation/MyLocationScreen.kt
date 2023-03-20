package com.example.camperpro.android.myLocation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.AppButtonSmall
import com.example.camperpro.android.composables.PopupParkingLocation
import com.example.camperpro.android.extensions.hasLocationPermission
import com.example.camperpro.android.extensions.navigateByGmaps
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.Marker
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.Globals
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun myLocation(navigator: DestinationsNavigator, viewModel: MyLocationViewModel = getViewModel()) {

    val popupIsActive by viewModel.popupIsActiveFlow.collectAsState()

    Box {
        Column {
            Header(navigator = navigator)
            MyLocationMap(viewModel.parkingLocationFlow,
                          viewModel.currentLocationFlow,
                          selectCurrentLocation = { viewModel.selectCurrentLocation() },
                          selectParkingLocation = { viewModel.selectParkingLocation() },
                          parkHere = { viewModel.setParkingLocation(it.latitude, it.longitude) },
                          deleteParkingLoc = { viewModel.deleteParkingLocation() })
        }
        if (popupIsActive.value) PopupParkingLocation(
            Modifier,
            onExit = { popupIsActive.value = false },
            onDelete = {
                popupIsActive.value = false
                viewModel.deleteParkingLocation()
            })
    }
}


@Composable
fun Header(navigator: DestinationsNavigator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navigator.popBackStack() }) {
            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
        }
        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            text = stringResource(id = R.string.menu_my_location),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun MyLocationMap(
    parkingLocationFlow: MutableStateFlow<MutableState<Marker>>,
    currentLocationFlow: MutableStateFlow<MutableState<Marker>>,
    selectCurrentLocation: () -> Unit,
    selectParkingLocation: () -> Unit,
    parkHere: (Location) -> Unit,
    deleteParkingLoc: () -> Unit
) {

    val parkingLocation by parkingLocationFlow.collectAsState()
    val currentLocation by currentLocationFlow.collectAsState()

    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = if (context.hasLocationPermission) {
            CameraPosition.fromLatLngZoom(
                LatLng(
                    Globals.geoLoc.lastKnownLocation.latitude,
                    Globals.geoLoc.lastKnownLocation.longitude
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

    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false, myLocationButtonEnabled = false
            ),
            cameraPositionState = cameraPositionState
        ) {
            if (parkingLocation.value.latitude != 0.0) {
                Marker(icon = if (parkingLocation.value.selected) {
                    BitmapDescriptorFactory.fromResource(R.drawable.marker_selected)
                } else {
                    BitmapDescriptorFactory.fromResource(R.drawable.marker)
                }, state = MarkerState(
                    position = LatLng(
                        parkingLocation.value.latitude, parkingLocation.value.longitude
                    )
                ), onClick = {
                    selectParkingLocation()
                    true
                })
            }

            if (currentLocation.value.latitude != 0.0) {
                Marker(icon = if (currentLocation.value.selected) {
                    BitmapDescriptorFactory.fromResource(R.drawable.marker_selected)
                } else {
                    BitmapDescriptorFactory.fromResource(R.drawable.marker)
                }, state = MarkerState(
                    position = LatLng(
                        currentLocation.value.latitude, currentLocation.value.longitude
                    )
                ), onClick = {
                    selectCurrentLocation()
                    true
                })
            }
        }

        if (currentLocation.value.selected) {
            LocationInfosBox(
                modifier = Modifier.align(Alignment.BottomCenter),
                marker = currentLocation.value,
                {}) {
                parkHere(
                    Location(
                        currentLocation.value.latitude,
                        currentLocation.value.longitude
                    )
                )
            }
        } else if (parkingLocation.value.selected) {
            LocationInfosBox(
                modifier = Modifier.align(Alignment.BottomCenter),
                marker = parkingLocation.value,
                {
                    deleteParkingLoc()
                }) {
                context.navigateByGmaps(
                    context,
                    parkingLocation.value.latitude,
                    parkingLocation.value.longitude
                )
            }
        }

        //            AdContainer(pub = listOf<Ad>())
    }
}

@Composable
fun LocationInfosBox(
    modifier: Modifier,
    marker: Marker,
    appButton: () -> Unit,
    deleteParkingLoc: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(25.dp)
            .background(Color.White, RoundedCornerShape(8))
            .padding(15.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (marker.placeLinkedId == "parking") {
                    stringResource(id = R.string.my_parking)
                } else {
                    stringResource(id = R.string.my_location)
                },
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                color = AppColor.Tertiary
            )
            Spacer(modifier = Modifier.weight(1f))
            if (marker.placeLinkedId == "parking") {
                IconButton(onClick = {
                    deleteParkingLoc()
                }) {
                    Image(imageVector = Icons.Sharp.ThumbUp, contentDescription = "")
                }
            }
            IconButton(onClick = { }) {
                Image(imageVector = Icons.Sharp.ThumbUp, contentDescription = "")
            }
        }
        if (marker.placeLinkedId == "parking") {
            Row {
                Icon(imageVector = Icons.Sharp.ThumbUp, contentDescription = "")
                Text(text = "distance")
            }
        }
        Row {
            Icon(imageVector = Icons.Sharp.ThumbUp, contentDescription = "")
            Text(
                text = "adresse", fontSize = 12.sp, fontWeight = FontWeight(450), color = Color.Gray
            )
        }
        Row(Modifier.padding(top = 8.dp)) {
            Icon(
                imageVector = Icons.Sharp.ThumbUp,
                contentDescription = "",
            )
            Text(
                text = "location",
                fontSize = 12.sp,
                fontWeight = FontWeight(450),
                color = Color.Gray
            )
        }

        AppButtonSmall(
            onClick = { appButton() },
            color = AppColor.Secondary,
            modifier = Modifier.padding(top = 16.dp),
            drawableRes = if (marker.placeLinkedId == "parking") {
                R.drawable.park
            } else {
                R.drawable.here
            },
            textRes = if (marker.placeLinkedId == "parking") {
                R.string.navigate
            } else {
                R.string.park_here
            }
        )
    }
}

@Composable
fun AdContainer(ad: List<Ad>) {

    val uriHandler = LocalUriHandler.current

    GlideImage(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .clickable {
            uriHandler.openUri(ad.first().url)
            uriHandler.openUri(ad.first().click)
        }, imageModel = { ad.first().url }, imageOptions = ImageOptions(
        contentScale = ContentScale.FillBounds, alignment = Alignment.Center
    )
    )
}