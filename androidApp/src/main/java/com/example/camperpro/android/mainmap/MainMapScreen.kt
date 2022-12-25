@file:OptIn(ExperimentalMaterialApi::class)

package com.example.camperpro.android.mainmap

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.MyApplicationTheme
import com.example.camperpro.android.R
import com.example.camperpro.android.components.locationVo
import com.example.camperpro.android.destinations.SpotSheetDestination
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.domain.model.isAroundLastSearchedLocation
import com.example.camperpro.utils.BottomSheetOption
import com.example.camperpro.utils.Globals
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

    val scope = rememberCoroutineScope()

    val appViewModel = LocalDependencyContainer.current.appViewModel
    val refreshSpotsAroundMe = appViewModel.userClickedAroundMe.collectAsState()

    LaunchedEffect(true) {
        viewModel.getAds()
    }

    //    if (refreshSpotsAroundMe.value) {
    //        viewModel.getSpots(cameraPositionState.locationVo)
    //    }

    //    if (!spotsLoaded.value) {
    //        Globals.geoLoc.lastKnownLocation?.let { viewModel.getSpots(it) }
    //        spotsLoaded.value = true
    //    }

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
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false, myLocationButtonEnabled = false
            ),
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
                if (!cameraPositionState.isMoving && !cameraPositionState.locationVo.isAroundLastSearchedLocation) {
                    SearchHereButton(onClick = {
                        scope.launch {
                            viewModel.showSpots(cameraPositionState.locationVo)
                        }
                    })
                }
                if (state.spots.isNotEmpty()) HorizontalSpotsList(spots = state.spots)
                if (state.ads.isNotEmpty()) MainMapAdContainer(state.ads)
            }
        }



        if (state.verticalListIsShowing) VerticalSpotsList(spots = state.spots)

        TopButtons(
            isVerticalListOpen = state.verticalListIsShowing,
            onListButtonClick = { viewModel.swapVerticalList() }
        )

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
fun SearchHereButton(onClick: () -> Unit) {

    val scope = rememberCoroutineScope()

    Row(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .height(40.dp),
            shape = RoundedCornerShape(Dimensions.radiusAppButton),
            onClick = { scope.launch { onClick() } },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppColor.Secondary
            )
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically), text = stringResource(
                    id = R.string.search_this_area
                ), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.W500
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
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
            .background(Color.White)
            .fillMaxSize()
            .padding(top = 100.dp),
    ) {
        items(spots) { item ->
            VerticalListItem(item)
        }
    }
}

@Composable
fun VerticalListItem(spot: Spot) {
    Row(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, bottom = 20.dp)
            .shadow(2.dp, RoundedCornerShape(8))
            .zIndex(1f)
            .fillMaxWidth()
            .height(130.dp)
            .background(Color.White, RoundedCornerShape(8))
    ) {
        if (spot.isPremium) {

            Box {
                Image(
                    painter = painterResource(id = R.drawable.dealerex),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        )
                    )
                )

                Image(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 5.dp, bottom = 5.dp)
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp)

                )
            }
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            text = spot.name
        )
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight(450),
            fontSize = 10.sp, text = spot.name,
            color = AppColor.neutralText
        )
        Spacer(modifier = Modifier.weight(1f))

        Row {
            if (spot.isPremium && spot.photos.isNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.premium_badge),
                    contentDescription = "",
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp)
                )
            }

            if (spot.isPremium) {
                Image(
                    painter = painterResource(id = R.drawable.repair),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp)
                )
            }

            if (spot.isPremium) {
                Image(
                    painter = painterResource(id = R.drawable.dealers),
                    contentDescription = "",
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp)
                )
            }

            if (spot.isPremium) {
                Image(
                    painter = painterResource(id = R.drawable.accessories),
                    contentDescription = "",
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .shadow(2.dp, RoundedCornerShape(15))
                    .zIndex(1f)
                    .background(Color.White, RoundedCornerShape(15))
                    .padding(5.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 5.dp),
                    painter = painterResource(id = R.drawable.distance),
                    contentDescription = "", tint = AppColor.Primary
                )
                Text(
                    text = "30 km",
                    color = AppColor.neutralText,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
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
            .fillMaxWidth()
            .height(130.dp)
    ) {
        items(spots) { item ->
            HorizontalListItem(item)
        }
    }
}

@Composable
fun LazyItemScope.HorizontalListItem(spot: Spot) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .shadow(2.dp, RoundedCornerShape(8))
            .zIndex(1f)
            .fillMaxHeight()
            .fillParentMaxWidth(0.85f)
            .background(Color.White, RoundedCornerShape(8))
    ) {

        if (spot.isPremium && spot.photos.isNotEmpty()) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.dealerex),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        )
                    )
                )

                Image(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 5.dp, bottom = 5.dp)
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp)
                )
            }
        }

        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                text = spot.name
            )
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                fontWeight = FontWeight(450),
                fontSize = 10.sp, text = spot.name,
                color = AppColor.neutralText
            )
            Spacer(modifier = Modifier.weight(1f))

            Row {
                if (spot.isPremium) {
                    Image(
                        painter = painterResource(id = R.drawable.premium_badge),
                        contentDescription = "",
                        modifier = Modifier
                            .shadow(2.dp, RoundedCornerShape(15))
                            .zIndex(1f)
                            .background(Color.White, RoundedCornerShape(15))
                            .padding(5.dp)
                    )
                }

                if (spot.isPremium) {
                    Image(
                        painter = painterResource(id = R.drawable.repair),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .shadow(2.dp, RoundedCornerShape(15))
                            .zIndex(1f)
                            .background(Color.White, RoundedCornerShape(15))
                            .padding(5.dp)
                    )
                }

                if (spot.isPremium) {
                    Image(
                        painter = painterResource(id = R.drawable.dealers),
                        contentDescription = "",
                        modifier = Modifier
                            .shadow(2.dp, RoundedCornerShape(15))
                            .zIndex(1f)
                            .background(Color.White, RoundedCornerShape(15))
                            .padding(5.dp)
                    )
                }

                if (spot.isPremium) {
                    Image(
                        painter = painterResource(id = R.drawable.accessories),
                        contentDescription = "",
                        modifier = Modifier
                            .shadow(2.dp, RoundedCornerShape(15))
                            .zIndex(1f)
                            .background(Color.White, RoundedCornerShape(15))
                            .padding(5.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 5.dp),
                        painter = painterResource(id = R.drawable.distance),
                        contentDescription = "", tint = AppColor.Primary
                    )
                    Text(
                        text = "30 km",
                        color = AppColor.neutralText,
                        fontWeight = FontWeight.W500,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MainMapAdContainer(ad: List<Ad>) {

    val scope = rememberCoroutineScope()

    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { scope.launch { ad.first().click } },
        imageModel = { ad.first().url },
        imageOptions = ImageOptions(
            contentScale = ContentScale.FillBounds, alignment = Alignment.Center
        )
    )
}

@Composable
fun TopButtons(
    onListButtonClick: () -> Unit,
    isVerticalListOpen: Boolean
) {
    val appViewModel = LocalDependencyContainer.current.appViewModel
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = Dimensions.appMargin, end = Dimensions.appMargin)
    ) {

        IconButton(modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
            .zIndex(1f)
            .background(
                Color.White, RoundedCornerShape(Dimensions.radiusRound)
            ), onClick = {
            onListButtonClick()
        }) {
            Image(
                painter = if (isVerticalListOpen) painterResource(id = R.drawable.map) else painterResource(
                    id = R.drawable.list
                ),
                contentDescription = stringResource(id = R.string.cd_button_vertical_list)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(modifier = Modifier
            .padding(end = 10.dp)
            .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
            .zIndex(1f)
            .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
                   onClick = {
                       scope.launch {
                           if (isVerticalListOpen) Globals.currentBottomSheetOption =
                               BottomSheetOption.SORT else Globals.currentBottomSheetOption =
                               BottomSheetOption.FILTER
                           appViewModel.bottomSheetIsShowing.show()
                       }
                   }) {
            Image(
                painter = if (isVerticalListOpen) painterResource(id = R.drawable.sort) else painterResource(
                    id = R.drawable.map_layer
                ),
                contentDescription = stringResource(
                    id = R.string.cd_button_mapstyle
                )
            )
        }

        IconButton(modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
            .zIndex(1f)
            .background(
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