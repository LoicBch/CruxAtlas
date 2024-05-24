package com.example.camperpro.android.myLocation

import android.app.Application
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.example.camperpro.domain.model.composition.LocationInfos
import com.example.camperpro.domain.model.composition.Marker
import com.example.camperpro.domain.model.composition.distanceFromUserLocationText
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.Globals
import com.example.camperpro.utils.KMMPreference
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
    val activeLocation by viewModel.activeLocation.collectAsState()
    val application = LocalContext.current.applicationContext

    LaunchedEffect(key1 = true) {
        viewModel.init(application as Application)
    }

    Box {
        Column {
            Header(navigator = navigator)
            MyLocationMap(
                viewModel.parkingLocationFlow,
                viewModel.currentLocationFlow,
                selectCurrentLocation = { viewModel.selectCurrentLocation() },
                selectParkingLocation = { viewModel.selectParkingLocation(Location(0.0, 0.0)) },
                parkHere = {
                    viewModel.saveParkingLocation(
                        application as Application,
                        it.latitude,
                        it.longitude
                    )
                },
                deleteParkingLoc = { popupIsActive.value = true },
                activeLocation = activeLocation,
            )
        }
        if (popupIsActive.value) PopupParkingLocation(
            Modifier,
            onExit = { popupIsActive.value = false },
            onDelete = {
                popupIsActive.value = false
                viewModel.deleteParkingLocation(application as Application)
            })
    }
}


@Composable
fun Header(navigator: DestinationsNavigator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navigator.popBackStack() }) {
            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
        }
        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            text = stringResource(id = R.string.menu_my_location),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(0.5f))

        IconButton(modifier = Modifier.alpha(0f), onClick = { }) {
            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
        }
    }
}

@Composable
fun MyLocationMap(
    parkingLocationFlow: MutableStateFlow<MutableState<Marker>>,
    currentLocationFlow: MutableStateFlow<MutableState<Marker>>,
    selectCurrentLocation: () -> Unit,
    selectParkingLocation: () -> Unit,
    parkHere: (Location) -> Unit,
    activeLocation: LocationInfos,
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
                appButton = {
                    parkHere(
                        Location(
                            currentLocation.value.latitude,
                            currentLocation.value.longitude
                        )
                    )
                }, activeLocation = activeLocation,
                deleteParkingLoc = {}
            )
        } else if (parkingLocation.value.selected) {
            LocationInfosBox(
                modifier = Modifier.align(Alignment.BottomCenter),
                marker = parkingLocation.value,
                appButton = {
                    context.navigateByGmaps(
                        context,
                        parkingLocation.value.latitude,
                        parkingLocation.value.longitude
                    )

                },
                activeLocation = activeLocation,
                deleteParkingLoc = { deleteParkingLoc() }
            )
        }
    }
}

@Composable
fun LocationInfosBox(
    modifier: Modifier,
    marker: Marker,
    appButton: () -> Unit,
    activeLocation: LocationInfos,
    deleteParkingLoc: () -> Unit
) {

    val application = LocalContext.current.applicationContext as Application

    Column(
        modifier = modifier
            .padding(25.dp)
            .background(Color.White, RoundedCornerShape(8))
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp, top = 5.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (marker.placeLinkedId == "parking") {
                    stringResource(id = R.string.my_parking)
                } else {
                    stringResource(id = R.string.my_location)
                },
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                color = AppColor.Tertiary
            )
            Spacer(modifier = Modifier.weight(1f))
            if (marker.placeLinkedId == "parking") {
                IconButton(onClick = {
                    deleteParkingLoc()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.trash),
                        contentDescription = "",
                        tint = AppColor.Tertiary
                    )
                }
            }
            IconButton(onClick = { }) {
                Image(painter = painterResource(id = R.drawable.share), contentDescription = "")
            }
        }
        if (marker.placeLinkedId == "parking") {
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Icon(
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .size(20.dp),
                    painter = painterResource(id = R.drawable.distance),
                    tint = AppColor.Tertiary,
                    contentDescription = ""
                )
                Text(
                    text = Location(
                        marker.latitude,
                        marker.longitude
                    ).distanceFromUserLocationText(KMMPreference(application)),
                    fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                    fontWeight = FontWeight(450),
                    color = Color.Gray
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(20.dp),
                painter = painterResource(id = R.drawable.pin_here),
                contentDescription = "", tint = AppColor.Tertiary
            )
            Text(
                text = activeLocation.address,
                fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontWeight = FontWeight(450),
                color = Color.Gray
            )
        }
        Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(20.dp),
                painter = painterResource(id = R.drawable.location_arrow),
                contentDescription = "",
            )
            Text(
                text = "${activeLocation.gpsDeciTxt} (lat, lng) \n ${activeLocation.gpsDmsTxt} (lat, lng)",
                fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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