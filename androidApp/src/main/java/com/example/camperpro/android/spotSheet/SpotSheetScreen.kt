package com.example.camperpro.android.spotSheet

import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.AppButton
import com.example.camperpro.android.extensions.dial
import com.example.camperpro.android.extensions.navigateByGmaps
import com.example.camperpro.android.extensions.sendMail
import com.example.camperpro.android.extensions.share
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.Location
import com.example.camperpro.domain.model.Photo
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.domain.model.distanceFromUserLocationText
import com.example.camperpro.utils.Globals
import com.example.camperpro.utils.fullGeolocalisation
import com.example.camperpro.utils.fullLocation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun SpotSheet(navigator: DestinationsNavigator, spot: Spot) {
    Column {
        Header(spot, onClose = { navigator.popBackStack() })
        BaseInfos(Modifier.padding(horizontal = 16.dp), spot)
        Tabs(spot)
    }
}

@Composable
fun Header(spot: Spot, onClose: () -> Unit) {

    val context = LocalContext.current

    Box(Modifier.heightIn(min = 50.dp)) {
        if (spot.isPremium && spot.photos.isNotEmpty()) {
            Gallery(photos = spot.photos)
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
        ) {
            IconButton(modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
                .zIndex(1f)
                .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { onClose() }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
                .zIndex(1f)
                .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.help), contentDescription = "")
            }

            IconButton(modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
                .zIndex(1f)
                .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { context.share(context, "") }) {
                Icon(painter = painterResource(id = R.drawable.share), contentDescription = "")
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
        ) {
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

            // TODO:
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

            // TODO:
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

            if (spot.isPremium) {
                Row(
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 5.dp),
                        painter = painterResource(id = R.drawable.premium_badge),
                        contentDescription = "",
                        tint = AppColor.Primary
                    )
                    Text(
                        text = stringResource(id = R.string.verified),
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
fun Gallery(photos: List<Photo>) {
    val scrollState = rememberScrollState()

    LazyRow(
        modifier = Modifier.scrollable(
            state = scrollState, orientation = Orientation.Vertical
        )
    ) { //            items(spot.photos) {
    }
}

@Composable
fun BaseInfos(modifier: Modifier, spot: Spot) {
    Text(
        modifier = modifier.padding(bottom = 12.dp),
        text = "#${spot.id}",
        fontSize = 11.sp,
        fontWeight = FontWeight.W500,
        color = Color.Black
    )
    Text(
        modifier = modifier.padding(bottom = 16.dp),
        text = spot.name,
        fontSize = 22.sp,
        fontWeight = FontWeight.W700,
        color = Color.Black
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(spot: Spot) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val tabScreensTitle = listOf(
        stringResource(id = R.string.overview),
        stringResource(id = R.string.details),
        stringResource(id = R.string.contact_info)
    )

    val density = LocalDensity.current

    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabScreensTitle.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    Column(Modifier.padding(horizontal = 16.dp)) {
        TabRow(selectedTabIndex = pagerState.currentPage,
               backgroundColor = Color.Transparent,
               contentColor = AppColor.Primary,
               indicator = {
                   TabRowDefaults.Indicator(
                       modifier = Modifier.customTabIndicator(
                           currentTabPosition = it[pagerState.currentPage],
                           tabWidth = tabWidths[pagerState.currentPage]
                       )
                   )
               }) {

            tabScreensTitle.forEachIndexed { index, tab ->
                Tab(
                    text = {
                        Text(
                            text = tab,
                            onTextLayout = {
                                tabWidths[pagerState.currentPage] =
                                    with(density) { it.size.width.toDp() }
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = if (pagerState.currentPage == index) AppColor.Primary else AppColor.Tertiary
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.scrollToPage(index) } },
                )
            }
        }

        HorizontalPager(
            count = tabScreensTitle.size,
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> OverviewTab(spot, pagerState)
                1 -> DetailsTab(spot)
                2 -> ContactTab(spot)
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OverviewTab(spot: Spot, pagerState: PagerState) {

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        if (spot.isPremium) {
            RowContactPremium(spot = spot)
        } else {
            RowContact(spot = spot)
        }
        LocationInfos(spot)

        if (spot.services.isNotEmpty()) {
            ServicesList(
                spot.services.take(3),
                {
                    scope.launch { pagerState.scrollToPage(1) }
                },
                R.string.view_all_services,
                title = R.string.services,
                subtitle = R.string.services_subtitle, false
            )
        }

        if (spot.brands.isNotEmpty()) {
            ItemsList(
                spot.services.take(3),
                { scope.launch { pagerState.scrollToPage(1) } },
                R.string.view_all_brands,
                title = R.string.official_dealers,
                subtitle = R.string.dealers_subtitle, false
            )
        }

        if (spot.brands.isNotEmpty()) {
            ItemsList(
                spot.services.take(3),
                { scope.launch { pagerState.scrollToPage(1) } },
                R.string.view_all_services,
                title = R.string.accessories,
                subtitle = R.string.accessories_subtitle, false
            )
        }

        AppButton(
            isActive = true,
            onClick = { context.navigateByGmaps(context, spot.latitude, spot.longitude) },
            modifier = Modifier.padding(top = 50.dp),
            textRes = R.string.navigate
        )
    }
}

@Composable
fun ServicesList(
    items: List<String>,
    onExtend: () -> Unit,
    linkLabel: Int,
    title: Int,
    subtitle: Int,
    isFullList: Boolean
) {
    Text(
        modifier = Modifier.padding(top = 15.dp),
        text = stringResource(id = title),
        fontSize = 16.sp,
        fontWeight = FontWeight.W700,
        color = Color.Black
    )
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = stringResource(id = subtitle),
        fontSize = 12.sp,
        fontWeight = FontWeight(450),
        color = Color.Black
    )
    Divider(Modifier.padding(top = 8.dp))
    items.forEach { serviceIndex ->
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = Globals.filters.services.find { it.first == serviceIndex }?.second.toString(),
            color = AppColor.Tertiary,
            fontSize = 14.sp,
            fontWeight = FontWeight(450)
        )
    }
    if (!isFullList) {
        Row(modifier = Modifier.clickable { onExtend() }) {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = linkLabel),
                color = AppColor.Secondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "",
                    tint = AppColor.Secondary
                )
            }
        }
    }
}


@Composable
fun ItemsList(
    items: List<String>,
    onExtend: () -> Unit,
    @StringRes linkLabel: Int,
    @StringRes title: Int,
    @StringRes subtitle: Int,
    isFullList: Boolean
) {
    // TODO: side effect
    val rowCount = if (items.size < 3) {
        1
    } else {
        items.size / 3
    }

    Text(
        modifier = Modifier.padding(top = 20.dp),
        text = stringResource(id = title),
        fontSize = 16.sp,
        fontWeight = FontWeight.W700,
        color = Color.Black
    )
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = stringResource(id = subtitle),
        fontSize = 12.sp,
        fontWeight = FontWeight(450),
        color = Color.Black
    )
    Divider(Modifier.padding(top = 8.dp))

    val itemsSplit = items.chunked(rowCount)

    repeat(rowCount) {
        Row {
            itemsSplit[it].forEach { brandIndex ->
                Text(
                    text = Globals.filters.brands.find { it.first == brandIndex }?.second!!,
                    modifier = Modifier
                        .padding(end = 10.dp, top = 10.dp)
                        .border(
                            BorderStroke(1.dp, AppColor.unSelectedFilter), RoundedCornerShape(15)
                        )
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(450),
                    color = AppColor.Tertiary
                )
            }
        }
    }

    if (!isFullList) {
        Row(modifier = Modifier.clickable { onExtend() }) {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = linkLabel),
                color = AppColor.Secondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "",
                    tint = AppColor.Secondary
                )
            }
        }
    }
}


@Composable
fun LocationInfos(spot: Spot) {
    Row(
        modifier = Modifier.padding(start = 5.dp, top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 15.dp),
            painter = painterResource(id = R.drawable.distance),
            contentDescription = "",
            tint = AppColor.Tertiary
        )
        Text(
            text = Location(spot.latitude, spot.longitude).distanceFromUserLocationText!!,
            color = Color.Black,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp
        )
    }

    Row(
        modifier = Modifier.padding(start = 5.dp, top = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 15.dp),
            painter = painterResource(id = R.drawable.pin_here),
            contentDescription = "",
            tint = AppColor.Tertiary
        )
        Text(
            text = spot.fullLocation,
            color = Color.Black,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp
        )
    }

    Row(
        modifier = Modifier.padding(start = 5.dp, top = 15.dp, bottom = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 15.dp),
            painter = painterResource(id = R.drawable.my_location),
            contentDescription = "",
            tint = AppColor.Tertiary
        )
        Text(
            text = spot.fullGeolocalisation,
            color = Color.Black,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RowContact(spot: Spot) {
    Row(modifier = Modifier.padding(top = 20.dp)) {
        if (spot.website.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(15))
                    .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound))
                    .padding(vertical = 5.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(id = R.drawable.website),
                    contentDescription = "",
                    tint = AppColor.Secondary
                )
                Text(
                    modifier = Modifier.padding(end = 5.dp),
                    text = stringResource(id = R.string.website).uppercase(),
                    color = AppColor.Secondary,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }

        if (spot.phone.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(15))
                    .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound))
                    .padding(vertical = 5.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = "",
                    tint = AppColor.Secondary
                )
                Text(
                    modifier = Modifier.padding(end = 5.dp),
                    text = stringResource(id = R.string.phone).uppercase(),
                    color = AppColor.Secondary,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }

    }
}

@Composable
fun RowContactPremium(spot: Spot) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 20.dp)
    ) {
        if (spot.website.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.website),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }

        if (spot.phone.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }

        if (spot.email.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.mail),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }

        if (spot.facebook.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }

        if (spot.twitter.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.twitter),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }

        if (spot.youtube.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.youtube),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun DetailsTab(spot: Spot) {
    Column(modifier = Modifier.fillMaxHeight()) {
        ServicesList(
            spot.services,
            {},
            R.string.view_all_services,
            title = R.string.services,
            subtitle = R.string.services_subtitle, true
        )
        ItemsList(
            spot.brands,
            {},
            R.string.view_all_brands,
            title = R.string.official_dealers,
            subtitle = R.string.dealers_subtitle, true
        )
        ItemsList(
            spot.services,
            {},
            R.string.view_all_services,
            title = R.string.accessories,
            subtitle = R.string.accessories_subtitle, true
        )
    }

}

@Composable
fun ContactTab(spot: Spot) {

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxHeight()) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.pin_here), contentDescription = "")
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = spot.fullLocation,
                fontSize = 12.sp,
                fontWeight = FontWeight(450)
            )
        }
        Divider()

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.my_location), contentDescription = "")
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = spot.fullGeolocalisation,
                fontSize = 12.sp,
                fontWeight = FontWeight(450)
            )
        }
        Divider()

        if (spot.website.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { uriHandler.openUri(spot.website) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.website), contentDescription = "")
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = spot.website,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }

        if (spot.email.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { context.sendMail(spot.email, "") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.mail), contentDescription = "")
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = spot.email,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }


        if (spot.phone.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { context.dial(spot.phone) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.phone), contentDescription = "")
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = spot.phone,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }



        if (spot.facebook.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { uriHandler.openUri(spot.facebook) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.facebook), contentDescription = "")
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = spot.facebook,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }



        if (spot.twitter.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { uriHandler.openUri(spot.twitter) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.twitter), contentDescription = "")
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = spot.twitter,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }


        if (spot.youtube.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { uriHandler.openUri(spot.youtube) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.youtube), contentDescription = "")
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = spot.youtube,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }
    }
}

fun Modifier.customTabIndicator(
    currentTabPosition: TabPosition, tabWidth: Dp
): Modifier = composed(inspectorInfo = debugInspectorInfo {
    name = "tabIndicatorOffset"
    value = currentTabPosition
}) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .background(AppColor.Primary)
        .height(2.dp)
        .width(currentTabWidth)
}



