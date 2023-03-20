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
import androidx.navigation.NavController
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.MyApplicationTheme
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.LoadingModal
import com.example.camperpro.android.destinations.AroundLocationScreenDestination
import com.example.camperpro.android.destinations.DealerDetailsScreenDestination
import com.example.camperpro.android.destinations.MenuScreenDestination
import com.example.camperpro.android.extensions.hasLocationPermission
import com.example.camperpro.android.extensions.isScrollingUp
import com.example.camperpro.android.extensions.lastVisibleItemIndex
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.*
import com.example.camperpro.domain.model.composition.*
import com.example.camperpro.managers.location.LocationManager
import com.example.camperpro.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
    locationSearchRecipient: ResultRecipient<AroundLocationScreenDestination, Place>,
    launchEventsSearchRecipient: ResultRecipient<MenuScreenDestination, Boolean>,
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: MainMapViewModel = getViewModel()
) {
    val state by viewModel.state.collectAsState()
    val locationSearched by viewModel.placeSearched.collectAsState()
    val currentlyLoading by viewModel.loading.collectAsState()
    val markersState by viewModel.markersFlow.collectAsState()
    val updateSource by viewModel.updateSource.collectAsState()

    val dealers by viewModel.dealers.collectAsState()
    val events by viewModel.events.collectAsState()

    var properties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL))
    }

    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position =
            if (!LocationManager.isPermissionAllowed() || !LocationManager.isLocationEnable()) {
                Log.d("LOCATION", "MainMap: ")
                CameraPosition.fromLatLngZoom(
                    LatLng(
                        Constants.DEFAULT_LOCATION.latitude, Constants.DEFAULT_LOCATION.longitude
                    ), 5f
                )
            } else {
                Log.d("LOCATION", Globals.geoLoc.lastKnownLocation.latitude.toString())
                CameraPosition.fromLatLngZoom(
                    LatLng(
                        Globals.geoLoc.lastKnownLocation.latitude,
                        Globals.geoLoc.lastKnownLocation.longitude
                    ), 15f
                )
            }
    }

    val appViewModel = LocalDependencyContainer.current.appViewModel

    LaunchedEffect(true) {
        if (updateSource == UpdateSource.DEFAULT) {
            viewModel.getAds()
            if (context.hasLocationPermission) {
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            Globals.geoLoc.lastKnownLocation.latitude,
                            Globals.geoLoc.lastKnownLocation.longitude
                        )
                    )
                )
                viewModel.showSpots(Globals.geoLoc.lastKnownLocation)
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
                                Globals.geoLoc.lastKnownLocation.latitude,
                                Globals.geoLoc.lastKnownLocation.longitude
                            )
                        )
                    )
                    viewModel.showSpots(Globals.geoLoc.lastKnownLocation)
                }
            }
        }
    }

    launchEventsSearchRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                viewModel.showEvents()
                appViewModel.onBottomSheetContentChange(BottomSheetOption.FILTER_EVENT)
            }
        }
    }

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
                viewModel.showSpotsAroundPlace(result.value)
            }
        }
    }

    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(), properties = properties, uiSettings = MapUiSettings(
                zoomControlsEnabled = false, myLocationButtonEnabled = false,
            ), cameraPositionState = cameraPositionState
        ) {
            markersState.forEach { marker ->
                val markerState = MarkerState(position = LatLng(marker.latitude, marker.longitude))
                Log.d("SELECTED", marker.selected.toString())
                Marker(icon = if (marker.selected) {
                    BitmapDescriptorFactory.fromResource(R.drawable.marker_selected)
                } else {
                    BitmapDescriptorFactory.fromResource(R.drawable.marker)
                }, state = markerState, onClick = {
                    if (updateSource == UpdateSource.EVENTS) {
                        true
                    } else {
                        navigator.navigate(
                            DealerDetailsScreenDestination(
                                dealers.find { it.id == marker.placeLinkedId }!!
                            )
                        )
                        true
                    }
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
                if (updateSource == UpdateSource.EVENTS) {
                    if (events.isNotEmpty()) {

                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    events.first().latitude,
                                    events.first().longitude
                                ), 15f
                            )
                        )
                        HorizontalEventsList(cameraPositionState = cameraPositionState,
                                             events = events,
                                             onItemClicked = {
                                                 navigator.navigate(
                                                     com.example.camperpro.android.destinations.EventDetailScreenDestination(
                                                         it
                                                     )
                                                 )
                                             },
                                             onScrollEnded = { event ->
                                                 viewModel.selectMarker(
                                                     markersState.indexOf(markersState.find { it.placeLinkedId == event.id })
                                                 )
                                             })
                    }
                } else {
                    if (dealers.isNotEmpty()) {
                        HorizontalSpotsList(cameraPositionState = cameraPositionState,
                                            spots = dealers,
                                            onItemClicked = {
                                                navigator.navigate(
                                                    DealerDetailsScreenDestination(
                                                        it
                                                    )
                                                )
                                            },
                                            onScrollEnded = { dealer ->
                                                viewModel.selectMarker(
                                                    markersState.indexOf(markersState.find { it.placeLinkedId == dealer.id })
                                                )
                                            })
                    }
                }
                if (state.ads.isNotEmpty()) MainMapAdContainer(state.ads)
            }
        }

        if (state.verticalListIsShowing) {
            if (updateSource == UpdateSource.EVENTS) {
                VerticalEventsList(eventSortedFlow = viewModel.eventsSorted,
                                   { viewModel.onSortingOptionSelected(it) }) {
                    navigator.navigate(
                        com.example.camperpro.android.destinations.EventDetailScreenDestination(
                            it
                        )
                    )
                }
            } else {
                VerticalDealersList(dealersSortedFlow = viewModel.dealersSorted,
                                    { viewModel.onSortingOptionSelected(it) }) {
                    navigator.navigate(
                        DealerDetailsScreenDestination(
                            it
                        )
                    )
                }
            }
        }

        // TODO: Remonter les params
        TopButtons(isVerticalListOpen = state.verticalListIsShowing,
                   onListButtonClick = {
                       viewModel.swapVerticalList()
                   },
                   source = updateSource,
                   onMapPropertiesUpdated = {
                       properties = if (properties.mapType == MapType.NORMAL) {
                           MapProperties(isMyLocationEnabled = true, mapType = MapType.SATELLITE)
                       } else {
                           MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)
                       }
                   })

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
fun LocationSearchContainer(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp, start = 16.dp, end = 16.dp)
            .background(Color.White, RoundedCornerShape(15))
            .padding(vertical = 12.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
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
            imageVector = Icons.Filled.Close, contentDescription = ""
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
fun VerticalEventsList(
    eventSortedFlow: StateFlow<List<Event>>,
    onSortOptionSelected: (SortOption) -> Unit,
    onItemClicked: (Event) -> Unit
) {

    val sorting = LocalDependencyContainer.current.appViewModel.verticalListSortingOption
    val eventSorted by eventSortedFlow.collectAsState()

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
        items(eventSorted) { item ->
            Row(modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, bottom = 20.dp)
                .shadow(2.dp, RoundedCornerShape(8))
                .zIndex(1f)
                .fillMaxWidth()
                .height(130.dp)
                .background(Color.White, RoundedCornerShape(8))
                .clickable { onItemClicked(item) }) {
                VerticalEventListItem(item)
            }
        }
    }
}

@Composable
fun VerticalEventListItem(event: Event) {

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.W500,
            maxLines = 1,
            fontSize = 14.sp,
            text = event.name
        )
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight(450),
            fontSize = 12.sp,
            maxLines = 1,
            text = "${event.dateBegin} - ${event.dateEnd}",
            color = AppColor.neutralText
        )
        Spacer(modifier = Modifier.weight(1f))

        Row {
            Row(
                modifier = Modifier
                    .shadow(2.dp, RoundedCornerShape(15))
                    .zIndex(1f)
                    .background(Color.White, RoundedCornerShape(15))
                    .padding(5.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.country,
                    color = AppColor.Primary,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
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
                    contentDescription = "",
                    tint = AppColor.Primary
                )
                Text(
                    text = Location(
                        event.latitude, event.longitude
                    ).distanceFromUserLocationText!!,
                    color = AppColor.Primary,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Composable
fun VerticalDealersList(
    dealersSortedFlow: StateFlow<List<Dealer>>,
    onSortOptionSelected: (SortOption) -> Unit,
    onItemClicked: (Dealer) -> Unit
) {

    val sorting = LocalDependencyContainer.current.appViewModel.verticalListSortingOption
    val dealersSorted by dealersSortedFlow.collectAsState()

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
        items(dealersSorted) { item ->
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
fun VerticalListItem(dealer: Dealer) {

    if (dealer.isPremium && dealer.photos.isNotEmpty()) {
        Box {
            GlideImage(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp, bottomStart = 8.dp
                        )
                    )
                    .size(130.dp),
                imageModel = { dealer.photos[0].url },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillHeight, alignment = Alignment.Center
                )
            )

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
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.W500,
            maxLines = 1,
            fontSize = 14.sp,
            text = dealer.name
        )
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight(450),
            fontSize = 12.sp,
            maxLines = 1,
            text = dealer.fullLocation,
            color = AppColor.neutralText
        )

        Spacer(modifier = Modifier.weight(1f))

        Row {
            if (dealer.isPremium && dealer.photos.isEmpty()) {
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

            if (dealer.services.isNotEmpty()) {
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

            if (dealer.brands.isNotEmpty()) {
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

            Spacer(modifier = Modifier.weight(1f))

            if (dealer.photos.isEmpty()) {
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
                            dealer.latitude, dealer.longitude
                        ).distanceFromUserLocationText!!,
                        color = AppColor.neutralText,
                        fontWeight = FontWeight.W500,
                        fontSize = 12.sp
                    )
                }
            }
        }
        if (dealer.isPremium && dealer.photos.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp)
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
                        dealer.latitude, dealer.longitude
                    ).distanceFromUserLocationText!!,
                    color = AppColor.neutralText,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// TODO: factoriser les rv
// TODO: infinite recomposition here  -> clean side effect
@Composable
fun HorizontalEventsList(
    cameraPositionState: CameraPositionState,
    events: List<Event>,
    onItemClicked: (Event) -> Unit,
    onScrollEnded: (Event) -> Unit
) {
    val listState = rememberLazyListState()
    val needToReposition by remember {
        derivedStateOf {
            !listState.isScrollInProgress && listState.firstVisibleItemScrollOffset != 0
        }
    }

    Column {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp), state = listState
        ) {
            items(events) { item ->
                Row(modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .shadow(2.dp, RoundedCornerShape(8))
                    .zIndex(1f)
                    .fillMaxHeight()
                    .fillParentMaxWidth(0.85f)
                    .background(Color.White, RoundedCornerShape(8))
                    .clickable { onItemClicked(item) }) {
                    HorizontalEventListItem(item)
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
                                    events[listState.firstVisibleItemIndex].latitude,
                                    events[listState.firstVisibleItemIndex].longitude
                                ), 15f
                            ), 1000
                        )
                        onScrollEnded(events[listState.firstVisibleItemIndex])
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
                                        events[listState.lastVisibleItemIndex!!].latitude,
                                        events[listState.lastVisibleItemIndex!!].longitude
                                    ), 15f
                                ), 1000
                            )
                            onScrollEnded(events[listState.lastVisibleItemIndex!!])
                        } else {
                            listState.animateScrollToItem(listState.firstVisibleItemIndex)
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        events[listState.firstVisibleItemIndex].latitude,
                                        events[listState.firstVisibleItemIndex].longitude
                                    ), 15f
                                ), 1000
                            )
                            onScrollEnded(events[listState.firstVisibleItemIndex])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalEventListItem(event: Event) {

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.W500,
            maxLines = 1,
            fontSize = 14.sp,
            text = event.name
        )

        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.W500,
            maxLines = 1,
            fontSize = 14.sp,
            text = "${event.dateBegin} - ${event.dateEnd}"
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = event.country,
                color = AppColor.neutralText,
                fontWeight = FontWeight.W500,
                fontSize = 12.sp
            )

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
                    contentDescription = "",
                    tint = AppColor.Primary
                )
                Text(
                    text = Location(
                        event.latitude, event.longitude
                    ).distanceFromUserLocationText!!,
                    color = AppColor.neutralText,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// TODO: infinite recomposition here  -> clean side effect
@Composable
fun HorizontalSpotsList(
    cameraPositionState: CameraPositionState,
    spots: List<Dealer>,
    onItemClicked: (Dealer) -> Unit,
    onScrollEnded: (Dealer) -> Unit
) {

    val listState = rememberLazyListState()
    val needToReposition by remember {
        derivedStateOf {
            !listState.isScrollInProgress && listState.firstVisibleItemScrollOffset != 0
        }
    }

    Column {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp), state = listState
        ) {
            items(items = spots, key = { spot -> spot.id }) { item ->
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
                                    spots[listState.firstVisibleItemIndex].latitude,
                                    spots[listState.firstVisibleItemIndex].longitude
                                ), 15f
                            ), 1000
                        )
                        onScrollEnded(spots[listState.firstVisibleItemIndex])
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
                                        spots[listState.lastVisibleItemIndex!!].latitude,
                                        spots[listState.lastVisibleItemIndex!!].longitude
                                    ), 15f
                                ), 1000
                            )
                            onScrollEnded(spots[listState.lastVisibleItemIndex!!])
                        } else {
                            listState.animateScrollToItem(listState.firstVisibleItemIndex)
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        spots[listState.firstVisibleItemIndex].latitude,
                                        spots[listState.firstVisibleItemIndex].longitude
                                    ), 15f
                                ), 1000
                            )
                            onScrollEnded(spots[listState.firstVisibleItemIndex])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalListItem(dealer: Dealer) {

    if (dealer.isPremium && dealer.photos.isNotEmpty()) {
        Box {

            GlideImage(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp, bottomStart = 8.dp
                        )
                    )
                    .size(130.dp),
                imageModel = { dealer.photos[0].url },
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
            text = dealer.name
        )
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight(450),
            maxLines = 1,
            fontSize = 12.sp,
            text = dealer.fullLocation,
            color = AppColor.neutralText
        )
        Spacer(modifier = Modifier.weight(1f))

        Row {
            if (dealer.isPremium && dealer.photos.isEmpty()) {
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

            if (dealer.services.isNotEmpty()) {
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

            if (dealer.brands.isNotEmpty()) {
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

            Spacer(modifier = Modifier.weight(1f))

            if (dealer.photos.isEmpty()) {
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
                            dealer.latitude, dealer.longitude
                        ).distanceFromUserLocationText!!,
                        color = AppColor.neutralText,
                        fontWeight = FontWeight.W500,
                        fontSize = 12.sp
                    )
                }
            }
        }
        if (dealer.isPremium && dealer.photos.isNotEmpty()) {
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
                        dealer.latitude, dealer.longitude
                    ).distanceFromUserLocationText!!,
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

@Composable
fun TopButtons(
    onListButtonClick: () -> Unit,
    isVerticalListOpen: Boolean,
    source: UpdateSource,
    onMapPropertiesUpdated: () -> Unit,
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
                modifier = Modifier.padding(10.dp),
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
            .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)), onClick = {
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
                    onMapPropertiesUpdated()
                }
            }
        }) {
            Image(
                modifier = Modifier.padding(10.dp),
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
                when (source) {
                    UpdateSource.EVENTS -> BottomSheetOption.FILTER_EVENT
                    else -> {
                        BottomSheetOption.FILTER
                    }
                }
                appViewModel.bottomSheetIsShowing.show()
            }

        }) {
            Image(
                modifier = Modifier.padding(10.dp),
                painter = painterResource(id = R.drawable.filter),
                contentDescription = stringResource(id = R.string.cd_button_filter)
            )
        }
    }
}

val CameraPositionState.locationVo
    get() = Location(
        this.position.target.latitude, this.position.target.longitude
    )

@Preview
@Composable
fun MainMapPreview() {
    MyApplicationTheme {}
}
