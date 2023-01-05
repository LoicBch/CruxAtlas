@file:OptIn(ExperimentalMaterialApi::class)

package com.example.camperpro.android.mainmap

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
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
import com.example.camperpro.android.destinations.AroundLocationScreenDestination
import com.example.camperpro.android.destinations.SpotSheetDestination
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.*
import com.example.camperpro.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@RootNavGraph(start = true)
@Destination
@Composable
fun MainMap(
    locationSearch: ResultRecipient<AroundLocationScreenDestination, Place>,
    navigator: DestinationsNavigator,
    viewModel: MainMapViewModel = getViewModel()
) {
    val state by viewModel.state.collectAsState()
    val locationSearched by viewModel.placeSearched.collectAsState()
    val context = LocalContext.current

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

    val appViewModel = LocalDependencyContainer.current.appViewModel

    LaunchedEffect(true) {
        if (state.spotRepresentation.source == SpotSource.DEFAULT) {
            viewModel.getAds()
            if (context.hasLocationPermission) {
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            Globals.geoLoc.lastKnownLocation?.latitude!!,
                            Globals.geoLoc.lastKnownLocation?.longitude!!
                        )
                    )
                )
                viewModel.showSpots(Globals.geoLoc.lastKnownLocation!!)
            } else {
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            Constants.DEFAULT_LOCATION.latitude,
                            Constants.DEFAULT_LOCATION.longitude
                        )
                    )
                )
                viewModel.showSpots(Constants.DEFAULT_LOCATION)
            }
        }

    }

    LaunchedEffect(appViewModel.loadAroundMeIsPressed) {
        appViewModel.loadAroundMeIsPressed.collect {
            if (it) {
                if (context.hasLocationPermission) {
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                Globals.geoLoc.lastKnownLocation?.latitude!!,
                                Globals.geoLoc.lastKnownLocation?.longitude!!
                            )
                        )
                    )
                    viewModel.showSpots(Globals.geoLoc.lastKnownLocation!!)
                } else { // TODO: display message
                }
            }
        }

    }

    locationSearch.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            result.value.location.latitude, result.value.location.longitude
                        )
                    )
                )
                viewModel.showSpotsAroundPlace(result.value)
            }
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
            state.spotRepresentation.spots.forEach { spot ->
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
                        viewModel.showSpots(cameraPositionState.locationVo)
                    })
                }

                if (locationSearched.isNotEmpty()) LocationSearchContainer(locationSearched)
                if (state.spotRepresentation.spots.isNotEmpty()) {
                    HorizontalSpotsList(spots = state.spotRepresentation.spots, onItemClicked = {
                        navigator.navigate(
                            SpotSheetDestination(
                                it
                            )
                        )
                    })
                }
                if (state.ads.isNotEmpty()) MainMapAdContainer(state.ads)
            }
        }

        if (state.verticalListIsShowing) {
            VerticalSpotsList(spotsSortedFlow = viewModel.sortedSpots,
                              { viewModel.onSortingOptionSelected(it) }) {
                navigator.navigate(
                    SpotSheetDestination(
                        it
                    )
                )
            }
        }


        TopButtons(
            isVerticalListOpen = state.verticalListIsShowing,
            onListButtonClick = { viewModel.swapVerticalList() },
            source = state.spotRepresentation.source
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
fun LocationSearchContainer(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(bottom = 15.dp, start = 16.dp, end = 16.dp)
            .background(Color.White, RoundedCornerShape(5)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(start = 14.dp),
            painter = painterResource(id = R.drawable.pin_here),
            contentDescription = "",
            tint = AppColor.Primary
        )

        Text(
            modifier = Modifier.padding(start = 22.dp),
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier.padding(end = 15.dp),
            imageVector = Icons.Filled.Close,
            contentDescription = ""
        )
    }
}

@Composable
fun SearchHereButton(onClick: () -> Unit) {

    Row(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .height(40.dp),
            shape = RoundedCornerShape(Dimensions.radiusAppButton),
            onClick = { onClick() },
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
fun VerticalSpotsList(
    spotsSortedFlow: StateFlow<List<Spot>>,
    onSortOptionSelected: (SortOption) -> Unit,
    onItemClicked: (Spot) -> Unit
) {

    val sorting = LocalDependencyContainer.current.appViewModel.verticalListSortingOption
    val spotsSorted by spotsSortedFlow.collectAsState()

    LaunchedEffect(sorting) {
        sorting.collect {
            onSortOptionSelected(it)
        }
    }

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
        items(spotsSorted) { item ->
            Row(modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, bottom = 20.dp)
                .shadow(2.dp, RoundedCornerShape(8))
                .zIndex(1f)
                .fillMaxWidth()
                .height(130.dp)
                .background(Color.White, RoundedCornerShape(8))
                .clickable { onItemClicked(item) }) {
                VerticalListItem(item)
            }
        }
    }
}

@Composable
fun VerticalListItem(spot: Spot) {

    if (spot.isPremium) {
        Box {
            GlideImage(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp, bottomStart = 8.dp
                        )
                    )
                    .size(130.dp),
                imageModel = { spot.photos[0].url },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillHeight, alignment = Alignment.Center
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
            maxLines = 1,
            fontSize = 14.sp,
            text = spot.name
        )
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight(450),
            fontSize = 12.sp,
            maxLines = 1,
            text = spot.fullLocation,
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

            if (spot.services.isNotEmpty()) {
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

            if (spot.brands.isNotEmpty()) {
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

            if (spot.brands.isNotEmpty()) {
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

            if (spot.photos.isEmpty()) {
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
                        contentDescription = "",
                        tint = AppColor.Primary
                    )
                    Text(
                        text = Location(
                            spot.latitude, spot.longitude
                        ).distanceFromUserLocationText!!,
                        color = AppColor.neutralText,
                        fontWeight = FontWeight.W500,
                        fontSize = 12.sp
                    )
                }
            }
        }
        if (spot.isPremium && spot.photos.isNotEmpty()) {
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
                    contentDescription = "",
                    tint = AppColor.Primary
                )
                Text(
                    text = Location(spot.latitude, spot.longitude).distanceFromUserLocationText!!,
                    color = AppColor.neutralText,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun HorizontalSpotsList(spots: List<Spot>, onItemClicked: (Spot) -> Unit) {
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
            Row(modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .shadow(2.dp, RoundedCornerShape(8))
                .zIndex(1f)
                .fillMaxHeight()
                .fillParentMaxWidth(0.85f)
                .background(Color.White, RoundedCornerShape(8))
                .clickable { onItemClicked(item) }) {
                HorizontalListItem(item)
            }
        }
    }
}

@Composable
fun HorizontalListItem(spot: Spot) {

    if (spot.isPremium && spot.photos.isNotEmpty()) {
        Box {

            GlideImage(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp, bottomStart = 8.dp
                        )
                    )
                    .size(130.dp),
                imageModel = { spot.photos[0].url },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillHeight, alignment = Alignment.Center
                )
            )

            Image(
                painter = painterResource(id = R.drawable.premium_badge),
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
            maxLines = 1,
            fontSize = 14.sp,
            text = spot.name
        )
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight(450),
            maxLines = 1,
            fontSize = 12.sp,
            text = spot.fullLocation,
            color = AppColor.neutralText
        )
        Spacer(modifier = Modifier.weight(1f))

        Row {
            if (spot.isPremium && spot.photos.isEmpty()) {
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

            if (spot.services.isNotEmpty()) {
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

            if (spot.brands.isNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.dealers),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp)
                )
            }

            if (spot.brands.isNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.accessories),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (spot.photos.isEmpty()) {
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
                        contentDescription = "",
                        tint = AppColor.Primary
                    )
                    Text(
                        text = Location(
                            spot.latitude, spot.longitude
                        ).distanceFromUserLocationText!!,
                        color = AppColor.neutralText,
                        fontWeight = FontWeight.W500,
                        fontSize = 12.sp
                    )
                }
            }
        }
        if (spot.isPremium && spot.photos.isNotEmpty()) {
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
                    contentDescription = "",
                    tint = AppColor.Primary
                )
                Text(
                    text = Location(spot.latitude, spot.longitude).distanceFromUserLocationText!!,
                    color = AppColor.neutralText,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun MainMapAdContainer(ad: List<Ad>) {

    val uriHandler = LocalUriHandler.current

    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                uriHandler.openUri(ad.first().url)
                uriHandler.openUri(ad.first().click)
                       },
        imageModel = { ad.first().url },
        imageOptions = ImageOptions(
            contentScale = ContentScale.FillBounds, alignment = Alignment.Center
        )
    )
}

@Composable
fun TopButtons(
    onListButtonClick: () -> Unit, isVerticalListOpen: Boolean, source: SpotSource
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
                ), contentDescription = stringResource(id = R.string.cd_button_vertical_list)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(modifier = Modifier
            .padding(end = 10.dp)
            .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
            .zIndex(1f)
            .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)), onClick = {
            scope.launch {
                appViewModel.onBottomSheetContentChange(
                    if (isVerticalListOpen) {
                        if (source == SpotSource.AROUND_PLACE) {
                            BottomSheetOption.SORT_AROUND_PLACE
                        }
                        BottomSheetOption.SORT
                    } else {
                        BottomSheetOption.FILTER
                    }
                )
                appViewModel.bottomSheetIsShowing.show()
            }
        }) {
            Image(
                painter = if (isVerticalListOpen) painterResource(id = R.drawable.sort) else painterResource(
                    id = R.drawable.map_layer
                ), contentDescription = stringResource(
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