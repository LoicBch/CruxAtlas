@file:OptIn(ExperimentalMaterialApi::class)

package com.example.camperpro.android.mainmap

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.MyApplicationTheme
import com.example.camperpro.android.R
import com.example.camperpro.android.components.Lotties.LoaderSpots
import com.example.camperpro.android.destinations.SpotSheetDestination
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.Spot
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun MainMap(navigator: DestinationsNavigator, viewModel: MainMapViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(45.7, 4.8), 10f)
    }
    val spotsLoaded = remember {
        mutableStateOf(false)
    }

    val appViewModel = LocalDependencyContainer.current.appViewModel
    val refreshSpotsAroundMe = appViewModel.userClickedAroundMe.collectAsState()

    if (refreshSpotsAroundMe.value) {
        viewModel.getSpots()
    }

    if (!spotsLoaded.value){
        viewModel.getSpots()
        spotsLoaded.value = true
    }

//    val imageAlpha: Float by animateFloatAsState(
//        targetValue = if (state.isLoading) 1f else 0f,
//        animationSpec = tween(
//            durationMillis = 500,
//            easing = LinearEasing,
//        )
//    )

    Log.d("Nav", "mainMapScreen is recomposed")


    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false),
            cameraPositionState = cameraPositionState
        ) {
            state.spots.forEach { spot ->
                Marker(state = MarkerState(position = LatLng(spot.latitude, spot.longitude)),
                    title = spot.name,
                    snippet = spot.name,
                    onClick = {
                        navigator.navigate(SpotSheetDestination(spot))
                        true
                    })
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                if (state.cameraIsOutOfRadiusLimit) {
                    SearchHereButton()
                }
                HorizontalSpotsList(spots = state.spots)
                PubContainer("")
            }
        }

        if (state.verticalListIsShowing) VerticalSpotsList(spots = state.spots)

        TopButtons(onListButtonClick = { viewModel.showVerticalList() })

//        Box(
//            modifier = Modifier
//                .alpha(imageAlpha)
//                .fillMaxSize()
//                .background(Color.Transparent), contentAlignment = Alignment.Center
//        ) {
//            LoaderSpots()
//        }
    }

    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
fun SearchHereButton() {
    Button(
        modifier = Modifier
            .clip(
                RoundedCornerShape(Dimensions.radiusAppButton)
            )
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 15.dp)
            .height(40.dp), onClick = {
            /*TODO*/
        }, colors = ButtonDefaults.buttonColors(
            backgroundColor = AppColor.BlueCamperPro
        )
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically), text = stringResource(
                id = R.string
                    .search_this_area
            ),
            color =
            Color.White
        )
    }
}

@Composable
fun VerticalSpotsList(spots: List<Spot>) {
    val scrollState = rememberScrollState()
    LazyColumn(
        modifier = Modifier
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
            .background(Color.LightGray)
            .fillMaxSize()
            .padding(top = 100.dp),
    ) {
        items(10) {
            VerticalListRow()
        }
    }
}

@Composable
fun VerticalListRow() {
    Row(
        modifier = Modifier
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth()
            .height(110.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(15))
    ) {
        Image(
            painter = painterResource(id = R.drawable.dealerex), contentDescription = "",
            modifier = Modifier
                .size(70.dp)
                .padding(5.dp)
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(modifier = Modifier.padding(bottom = 5.dp), text = "Lorem ipsum")
            Text(modifier = Modifier.padding(bottom = 5.dp), text = "Lorem ipsum")
            Text(modifier = Modifier.padding(bottom = 5.dp), text = "Lorem ipsum")
            Text(modifier = Modifier.padding(bottom = 5.dp), text = "Lorem ipsum")
        }
    }
}

@Composable
fun HorizontalSpotsList(spots: List<Spot>) {
    val scrollState = rememberScrollState()
    LazyRow(
        modifier = Modifier
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
            .background(Color.Green)
            .fillMaxWidth()
            .height(70.dp)
    ) {
        items(spots.size) {
            repeat(10) {

            }
        }
    }
}

@Composable
fun HorizontalListRow() {

}

@Composable
fun PubContainer(url: String) {
    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Yellow),
        imageModel = { url }, // loading a network image using an URL.
        imageOptions = ImageOptions(
            contentScale = ContentScale.FillBounds, alignment = Alignment.Center
        )
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopButtons(onListButtonClick: () -> Unit) {
    val appViewModel = LocalDependencyContainer.current.appViewModel
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = Dimensions.appMargin, end = Dimensions.appMargin)
    ) {

        IconButton(modifier = Modifier.background(
            Color.White, RoundedCornerShape(Dimensions.radiusRound)
        ), onClick = {
            onListButtonClick()
        }) {
            Image(
                painter = painterResource(id = R.drawable.list),
                contentDescription = stringResource(id = R.string.cd_button_vertical_list)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(modifier = Modifier
            .padding(end = 10.dp)
            .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
            onClick = { }) {
            Image(
                painter = painterResource(id = R.drawable.map_layer),
                contentDescription = stringResource(
                    id = R.string.cd_button_mapstyle
                )
            )
        }

        IconButton(modifier = Modifier.background(
            Color.White, RoundedCornerShape(Dimensions.radiusRound)
        ), onClick = {
            scope.launch {
                appViewModel.bottomSheetIsShowing.show()
            }
        }) {
            Image(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = stringResource(id = R.string.cd_button_filter)
            )
        }
    }
}

@Preview
@Composable
fun MainMapPreview() {
    MyApplicationTheme {}
}