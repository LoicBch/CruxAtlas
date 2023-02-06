package com.example.camperpro.android.myLocation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.ThumbUp
import androidx.compose.runtime.Composable
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
import com.example.camperpro.android.extensions.hasLocationPermission
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.Globals
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Destination
@Composable
fun myLocation(navigator: DestinationsNavigator) {

    Header(navigator = navigator)
    MyLocationMap()

}

@Composable
fun Header(navigator: DestinationsNavigator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp)
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
fun MyLocationMap() {

    val context = LocalContext.current
    val markers = listOf<Location>()
    val markerSelected = 2
    val cameraPositionState = rememberCameraPositionState {
        position = if (context.hasLocationPermission) {
            CameraPosition.fromLatLngZoom(
                LatLng(
                    Globals.geoLoc.lastKnownLocation?.latitude!!,
                    Globals.geoLoc.lastKnownLocation?.longitude!!
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
            markers.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                    onClick = {
                        it.isFlat
                    })
            }

            if (markerSelected != null) {
                LocationInfosBox(marker = markers.first())
            }

            //            AdContainer(pub = listOf<Ad>())
        }
    }
}

@Composable
fun LocationInfosBox(marker: Location) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "if",
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                color = AppColor.Tertiary
            )
            Spacer(modifier = Modifier.weight(1f))
            if (1 == 1) {
                IconButton(onClick = { }) {
                    Image(imageVector = Icons.Sharp.ThumbUp, contentDescription = "")
                }
            }
            IconButton(onClick = { }) {
                Image(imageVector = Icons.Sharp.ThumbUp, contentDescription = "")
            }
        }
        if (1 == 1) {
            Row {
                Icon(imageVector = Icons.Sharp.ThumbUp, contentDescription = "")
                Text(text = "distance")
            }
        }
        Row {
            Icon(imageVector = Icons.Sharp.ThumbUp, contentDescription = "")
            Text(
                text = "adresse",
                fontSize = 12.sp,
                fontWeight = FontWeight(450),
                color = Color.Gray
            )
        }
        Row {
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

        //        AppButton(isActive = true, onClick = { }, modifier = Modifier, textRes = "if")
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