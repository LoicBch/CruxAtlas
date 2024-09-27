@file:OptIn(ExperimentalMaterialApi::class)

package com.horionDev.climbingapp.android.mainmap

import android.app.Application
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.horionDev.climbingapp.android.LocalDependencyContainer
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.UnityParentActivity
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.composables.LoadingModal
import com.horionDev.climbingapp.android.composables.collectAsStateWithLifecycleImmutable
import com.horionDev.climbingapp.android.destinations.AroundLocationScreenDestination
import com.horionDev.climbingapp.android.destinations.MenuScreenDestination
import com.horionDev.climbingapp.android.extensions.isScrollingUp
import com.horionDev.climbingapp.android.extensions.lastVisibleItemIndex
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.android.ui.theme.Dimensions
import com.horionDev.climbingapp.domain.model.*
import com.horionDev.climbingapp.domain.model.composition.*
import com.horionDev.climbingapp.managers.location.LocationManager
import com.horionDev.climbingapp.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.horionDev.climbingapp.android.destinations.CragSheetDestination
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.ceuse
import com.horionDev.climbingapp.domain.model.entities.gradeDistributionString
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
    locationSearchRecipient: ResultRecipient<AroundLocationScreenDestination, Place>,
    launchEventsSearchRecipient: ResultRecipient<MenuScreenDestination, Boolean>,
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: MainMapViewModel = getViewModel()
) {
    val state by viewModel.state.collectAsState()
    val locationSearched by viewModel.placeSearched.collectAsState()
    val currentlyLoading by viewModel.loading.collectAsState()
    val markersState by viewModel.markersFlow.collectAsStateWithLifecycleImmutable()
    val updateSource by viewModel.updateSource.collectAsState()

    val dealers by viewModel.dealers.collectAsStateWithLifecycleImmutable()
    val events by viewModel.events.collectAsStateWithLifecycleImmutable()
    val laundry by viewModel.laundry.collectAsStateWithLifecycleImmutable()

    var mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = false, mapType = MapType.NORMAL))
    }

    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position =
            if (!LocationManager.isPermissionAllowed() || !LocationManager.isLocationEnable()) {
                CameraPosition.fromLatLngZoom(
                    LatLng(
                        Constants.DEFAULT_LOCATION.latitude, Constants.DEFAULT_LOCATION.longitude
                    ), 5f
                )
            } else {
                CameraPosition.fromLatLngZoom(
                    LatLng(
                        Globals.GeoLoc.lastKnownLocation.latitude,
                        Globals.GeoLoc.lastKnownLocation.longitude
                    ), 15f
                )
            }
    }

    val appViewModel = LocalDependencyContainer.current.appViewModel

    LaunchedEffect(true) { //        viewModel.event.collect {
        //            when (it) {
        //                is MainMapViewModel.MainMapEvent.UpdateRegion -> {
        //                    cameraPositionState.move(
        //                        CameraUpdateFactory.newLatLngBounds(
        //                            it.bounds,
        //                            it.padding
        //                        )
        //                    )
        //                }
        //            }
        //        }
    }

    LaunchedEffect(true) {

        //        if (updateSource == UpdateSource.DEFAULT) {
        ////            viewModel.getAds()
        //            if (context.hasLocationPermission) {
        //                cameraPositionState.move(
        //                    CameraUpdateFactory.newLatLng(
        //                        LatLng(
        //                            Globals.geoLoc.lastKnownLocation.latitude,
        //                            Globals.geoLoc.lastKnownLocation.longitude
        //                        )
        //                    )
        //                )
        //                viewModel.showLaundry(Globals.geoLoc.lastKnownLocation)
        //            } else {
        //                cameraPositionState.move(
        //                    CameraUpdateFactory.newLatLng(
        //                        LatLng(
        //                            Constants.DEFAULT_LOCATION.latitude,
        //                            Constants.DEFAULT_LOCATION.longitude
        //                        )
        //                    )
        //                )
        //            }
        //        }
    }

    LaunchedEffect(appViewModel.filtersApplied) {
        appViewModel.filtersApplied.collect {
//            viewModel.showSpots(cameraPositionState.locationVo, true)
        }
    }


//    LaunchedEffect(appViewModel.loadAroundMeIsPressed) { //        appViewModel.loadAroundMeIsPressed.collect {
//                    if (it) {
//                        if (context.hasLocationPermission) {
//                            cameraPositionState.move(
//                                CameraUpdateFactory.newLatLng(
//                                    LatLng(
//                                        Globals.geoLoc.lastKnownLocation.latitude,
//                                        Globals.geoLoc.lastKnownLocation.longitude
//                                    )
//                                )
//                            )
//                            viewModel.showLaundry(Globals.geoLoc.lastKnownLocation)
//                        }
//                    }
//                }
//    }

    locationSearchRecipient.onNavResult { result ->
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
//                viewModel.showSpotsAroundPlace(result.value)
            }
        }
    }


    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false, myLocationButtonEnabled = true,
            ),
            cameraPositionState = cameraPositionState
        ) {
            markersState.value.forEach { marker ->
                val markerState =
                    MarkerState(position = LatLng(marker.latitude, marker.longitude))
                Marker(icon = if (marker.selected) { //                    BitmapDescriptorFactory.fromResource(R.drawable.marker_selected)
                    BitmapDescriptorFactory.fromResource(R.drawable.marker)
                } else {
                    BitmapDescriptorFactory.fromResource(R.drawable.marker)
                }, state = markerState, onClick = {
                    navigator.navigate(
                        CragSheetDestination(
                            laundry.value.find { it.id.toString() == marker.placeLinkedId }!!
                        )
                    )
                    true
                })
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {

                //                if (!cameraPositionState.isMoving && !cameraPositionState.locationVo.isAroundLastSearchedLocation) {
                //                    SearchHereButton(onClick = {
                ////                        viewModel.showLaundry(cameraPositionState.locationVo)
                //                    }, cameraPositionState)
                //                }

                if (locationSearched.isNotEmpty()) LocationSearchContainer(locationSearched,
                    cameraPositionState,
                    {
                        viewModel.onCrossLocationClicked()
                    },
                    {
                        navigator.navigate(
                            AroundLocationScreenDestination()
                        )
                    })

                if (laundry.value.isNotEmpty()) {
                    HorizontalSpotsList(cameraPositionState = cameraPositionState,
                        crags = listOf(ceuse),
                        onItemClicked = {
                            navigator.navigate(
                                CragSheetDestination(it)
                            )
                        },
                        onScrollEnded = { dealer ->
                            viewModel.selectMarker(
                                markersState.value.indexOf(markersState.value
                                    .find {
                                        it.placeLinkedId ==
                                                dealer.id.toString()
                                    })
                            )
                        })
                }
            }
            if (state.ads.isNotEmpty()) MainMapAdContainer(state.ads)

        }

        if (state.verticalListIsShowing) {
            VerticalCragsList(dealersSortedFlow = viewModel.dealersSorted,
                {
//                                    viewModel.onSortingOptionSelected(it)
                })
            {
                //                    navigator.navigate(
                //                        LaundryDetailScreenDestination(
                //                            it
                //                        )
                //                    )
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            TopButtons(cameraPositionState = cameraPositionState,
                isVerticalListOpen = state.verticalListIsShowing,
                onListButtonClick = {
                    viewModel.swapVerticalList()
                },
                source = updateSource,
                sortingDealersFlow = appViewModel.verticalListSortingOption,
                sortingEventsFlow = appViewModel.verticalListSortingOption,
                onMapPropertiesUpdated = {
                    mapProperties = if (mapProperties.mapType == MapType.NORMAL) {
                        mapProperties.copy(mapType = MapType.SATELLITE)
                    } else {
                        mapProperties.copy(mapType = MapType.NORMAL)
                    }
                })

            if (!state.verticalListIsShowing) {
                Spacer(modifier = Modifier.weight(1f))
                BottomButtons(onAroundLocation = {
                    navigator.navigate(
                        AroundLocationScreenDestination()
                    )
                }, onAroundMe = {
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                Globals.GeoLoc.lastKnownLocation.latitude,
                                Globals.GeoLoc.lastKnownLocation.longitude
                            )
                        )
                    )
                })
            }
        }

        if (currentlyLoading) {
            LoadingModal(Modifier.align(Alignment.Center))
        }
    }


    BackHandler {
        if (navController.currentDestination?.route.toString() != "main_map") {
            navigator.popBackStack()
        }
    }
}

@Composable
fun BottomButtons(onAroundLocation: () -> Unit, onAroundMe: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 150.dp, end = 16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column {
            IconButton(
                modifier = Modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(25)
                ), onClick = {
                    onAroundLocation()
                }) {
                Image(
                    painter = painterResource(id = R.drawable.around_location),
                    contentDescription = stringResource(id = R.string.cd_button_vertical_list)
                )
            }

            IconButton(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(25)),
                onClick = {
                    onAroundMe()
                }) {
                Image(
                    painter = painterResource(id = R.drawable.around_me),
                    contentDescription = stringResource(id = R.string.cd_button_vertical_list)
                )
            }
        }
    }
}

@Composable
fun LocationSearchContainer(
    label: String,
    cameraPositionState: CameraPositionState,
    onClose: () -> Unit,
    onClick: () -> Unit
) {

    val overlayAlpha: Float by animateFloatAsState(
        targetValue = if (cameraPositionState.isMoving && cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) 0f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing,
        )
    )

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .alpha(overlayAlpha)
        .padding(bottom = 15.dp, start = 16.dp, end = 16.dp)
        .background(Color.White, RoundedCornerShape(15))
        .padding(vertical = 12.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.pin_here),
            contentDescription = "",
            tint = AppColor.Primary
        )

        Text(
            modifier = Modifier.padding(start = 22.dp),
            text = label,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight.W500,
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(imageVector = Icons.Filled.Close,
            contentDescription = "",
            modifier = Modifier.clickable {
                onClose()
            })
    }
}

@Composable
fun SearchHereButton(onClick: () -> Unit, cameraPositionState: CameraPositionState) {

    val overlayAlpha: Float by animateFloatAsState(
        targetValue = if (cameraPositionState.isMoving && cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) 0f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing,
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(overlayAlpha)
    ) {

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
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(
                    id = R.string.search_this_area
                ),
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontWeight = FontWeight.W500
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun VerticalCragsList(
    dealersSortedFlow: StateFlow<List<Crag>>,
    onSortOptionSelected: (SortOption) -> Unit,
    onItemClicked: (Crag) -> Unit
) {
    val sorting = LocalDependencyContainer.current.appViewModel.verticalListSortingOption
    val dealersSorted by dealersSortedFlow.collectAsStateWithLifecycleImmutable()
    val appViewModel = LocalDependencyContainer.current.appViewModel

    LaunchedEffect(sorting) {
        sorting.collect {
            onSortOptionSelected(it)
        }
    }

    LaunchedEffect(true) {
        onSortOptionSelected(appViewModel.verticalListSortingOption.value)
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
        items(dealersSorted.value) { item ->
            Row(modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, bottom = 20.dp, top = 5.dp)
                .shadow(2.dp, RoundedCornerShape(8))
                .zIndex(1f)
                .fillMaxWidth()
                .height(130.dp)
                .background(Color.White, RoundedCornerShape(8))
                .clickable { onItemClicked(item) }) {
                VerticalListItem(ceuse)
            }
        }
    }
}

@Composable
fun VerticalListItem(crag: Crag) {

    val application = LocalContext.current.applicationContext as Application

    if (crag.image.isNotEmpty()) {
        Box {
            GlideImage(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp, bottomStart = 8.dp
                        )
                    )
                    .size(130.dp),
                imageModel = { crag.image },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillHeight, alignment = Alignment.Center
                )
            )
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {

        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.W500,
            maxLines = 1,
            fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.oppinsedium)),
            text = crag.name,
        )

        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                fontWeight = FontWeight(450),
                maxLines = 1,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium)),
                text = "${crag.sectors.size} sectors",
                color = AppColor.neutralText
            )

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                fontWeight = FontWeight(450),
                maxLines = 1,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium)),
                text = "${
                    crag.sectors.map { it.routes.size }
                        .foldRight(0) { element, acc -> acc + element }
                } routes",
                color = AppColor.neutralText
            )

            Text(
                fontWeight = FontWeight(450),
                maxLines = 1,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium)),
                text = crag.gradeDistributionString(),
                color = AppColor.neutralText
            )
        }

//        val context = LocalContext.current
//        val launcher =
//            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
//                onResult = { result -> // Traiter le résultat ici
//                })
//
//        AppButton(
//            isActive = true,
//            onClick = {
//                val intent = Intent(context, UnityParentActivity::class.java).putExtra(
//                    "unity",
//                    "my_unity_scene"
//                )
//                launcher.launch(intent)
//            },
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//            textRes = R.string.unity_viewer
//        )

//        Spacer(modifier = Modifier.weight(1f))
//
//        Row {
//            Spacer(modifier = Modifier.weight(1f))
//        }
    }
}

// TODO: infinite recomposition here  -> clean side effect
@Composable
fun HorizontalSpotsList(
    cameraPositionState: CameraPositionState,
    crags: List<Crag>,
    onItemClicked: (Crag) -> Unit,
    onScrollEnded: (Crag) -> Unit
) {
    val listState = rememberLazyListState()
    val needToReposition by remember {
        derivedStateOf {
            !listState.isScrollInProgress && listState.firstVisibleItemScrollOffset != 0
        }
    }

    val overlayAlpha: Float by animateFloatAsState(
        targetValue = if (cameraPositionState.isMoving && cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) 0f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing,
        )
    )

    Column {
        LazyRow(
            modifier = Modifier
                .alpha(alpha = overlayAlpha)
                .fillMaxWidth()
                .height(130.dp),
            state = listState
        ) {
            items(items = crags, key = { crag -> crag.id!! }) { item ->
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

        when (listState.isScrollingUp()) {
            true -> {
                LaunchedEffect(needToReposition) {
                    if (needToReposition) {
                        listState.animateScrollToItem(listState.firstVisibleItemIndex)
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    crags[listState.firstVisibleItemIndex].latitude!!.toDouble(),
                                    crags[listState.firstVisibleItemIndex].longitude!!.toDouble()
                                ), 15f
                            ), 1000
                        )
                        onScrollEnded(crags[listState.firstVisibleItemIndex])
                    }
                }
            }

            false -> {
                LaunchedEffect(needToReposition) {
                    if (needToReposition) {
                        if (listState.firstVisibleItemScrollOffset > 400) {
                            listState.animateScrollToItem(listState.lastVisibleItemIndex!!)
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        crags[listState.lastVisibleItemIndex!!].latitude!!.toDouble(),
                                        crags[listState.lastVisibleItemIndex!!].longitude!!.toDouble()
                                    ), 15f
                                ), 1000
                            )
                            onScrollEnded(crags[listState.lastVisibleItemIndex!!])
                        } else {
                            listState.animateScrollToItem(listState.firstVisibleItemIndex)
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        crags[listState.firstVisibleItemIndex].latitude!!.toDouble(),
                                        crags[listState.firstVisibleItemIndex].longitude!!.toDouble()
                                    ), 15f
                                ), 1000
                            )
                            onScrollEnded(crags[listState.firstVisibleItemIndex])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalListItem(crag: Crag) {

    if (crag.image.isNotEmpty()) {
        Box {
            GlideImage(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp, bottomStart = 8.dp
                        )
                    )
                    .size(130.dp),
                imageModel = { crag.image },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillHeight, alignment = Alignment.Center
                )
            )
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {

        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.W500,
            maxLines = 1,
            fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.oppinsedium)),
            text = crag.name,
        )

        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                fontWeight = FontWeight(450),
                maxLines = 1,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium)),
                text = "${crag.sectors.size} sectors",
                color = AppColor.neutralText
            )

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                fontWeight = FontWeight(450),
                maxLines = 1,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium)),
                text = "${
                    crag.sectors.map { it.routes.size }
                        .foldRight(0) { element, acc -> acc + element }
                } routes",
                color = AppColor.neutralText
            )

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                fontWeight = FontWeight(450),
                maxLines = 1,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium)),
                text = crag.gradeDistributionString(),
                color = AppColor.neutralText
            )
        }


//        val context = LocalContext.current
//        val launcher =
//            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
//                onResult = { result -> // Traiter le résultat ici
//                })

//        AppButton(
//            isActive = true,
//            onClick = {
//                val intent = Intent(context, UnityParentActivity::class.java).putExtra(
//                    "unity",
//                    "my_unity_scene"
//                )
//                launcher.launch(intent)
//            },
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//            textRes = R.string.unity_viewer
//        )

//        Spacer(modifier = Modifier.weight(1f))
//
//        Row {
//            Spacer(modifier = Modifier.weight(1f))
//        }
    }
}

@Composable
fun MainMapAdContainer(ad: List<Ad>) {

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopButtons(
    cameraPositionState: CameraPositionState,
    onListButtonClick: () -> Unit,
    isVerticalListOpen: Boolean,
    source: UpdateSource,
    sortingDealersFlow: StateFlow<SortOption>,
    sortingEventsFlow: StateFlow<SortOption>,
    onMapPropertiesUpdated: () -> Unit,
) {
    val appViewModel = LocalDependencyContainer.current.appViewModel
    val scope = rememberCoroutineScope()

    val sortingDealers by sortingDealersFlow.collectAsState()
    val sortingEvents by sortingEventsFlow.collectAsState()

    val overlayAlpha: Float by animateFloatAsState(
        targetValue = if (cameraPositionState.isMoving && cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) 0f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing,
        )
    )

    Row(
        modifier = Modifier
            .alpha(alpha = overlayAlpha)
            .fillMaxWidth()
            .padding(top = 8.dp, start = Dimensions.appMargin, end = Dimensions.appMargin)
    ) {

        IconButton(onClick = {
            onListButtonClick()
        }) {
            Image(
                painter = if (isVerticalListOpen) painterResource(id = R.drawable.map_switch_round) else painterResource(
                    id = R.drawable.list_round
                ), contentDescription = stringResource(id = R.string.cd_button_vertical_list)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {
            appViewModel.withNetworkOnly {
                if (isVerticalListOpen) {
                    scope.launch {
                        appViewModel.onBottomSheetContentChange(
                            when (source) {
                                UpdateSource.AROUND_PLACE -> BottomSheetOption.SORT_AROUND_PLACE
                                UpdateSource.EVENTS -> BottomSheetOption.SORT_EVENTS
                                else -> {
                                    BottomSheetOption.SORT
                                }
                            }
                        )
                        appViewModel.bottomSheetIsShowing.show()
                    }
                } else {
                    onMapPropertiesUpdated.invoke()
                }
            }
        }) {
            Image(
                painter = if (isVerticalListOpen) {
                    if (sortingDealers != SortOption.NONE || sortingEvents != SortOption.NONE) {
                        painterResource(id = R.drawable.sort_active)
                    } else {
                        painterResource(id = R.drawable.sort_round)
                    }
                } else {
                    painterResource(
                        id = R.drawable.map_round
                    )
                }, contentDescription = stringResource(
                    id = R.string.cd_button_mapstyle
                )
            )
        }

        if (!appViewModel.isFiltersActive(updateSource = source)) {
            IconButton(onClick = {
                scope.launch {
                    appViewModel.onBottomSheetContentChange(
                        when (source) {
                            UpdateSource.EVENTS -> BottomSheetOption.FILTER_EVENT
                            else -> {
                                BottomSheetOption.FILTER
                            }
                        }
                    )
                    appViewModel.bottomSheetIsShowing.show()
                }

            }) {
                Image(
                    painter = painterResource(id = R.drawable.filter_round),
                    contentDescription = stringResource(id = R.string.cd_button_filter)
                )
            }
        } else {
            IconButton(onClick = {
                scope.launch {
                    appViewModel.onBottomSheetContentChange(
                        when (source) {
                            UpdateSource.EVENTS -> BottomSheetOption.FILTER_EVENT
                            else -> {
                                BottomSheetOption.FILTER
                            }
                        }
                    )
                    appViewModel.bottomSheetIsShowing.show()
                }

            }) {
                Image(
                    painter = painterResource(id = R.drawable.filter_active),
                    contentDescription = stringResource(id = R.string.cd_button_filter)
                )
            }
        }
    }
}

val CameraPositionState.locationVo
    get() = Location(
        this.position.target.latitude, this.position.target.longitude
    )

