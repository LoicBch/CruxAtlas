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
import androidx.compose.ui.platform.LocalDensity
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
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.Photo
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.utils.fullGeolocalisation
import com.example.camperpro.utils.fullLocation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination
@Composable
fun SpotSheet(spot: Spot) {
    Column {
        Header(spot)
        BaseInfos(Modifier.padding(horizontal = 16.dp), spot)
        Tabs(spot)
    }
}

@Composable
fun Header(spot: Spot) {
    Box(Modifier.heightIn(min = 50.dp)) {
        if (spot.isPremium && spot.photos.isNotEmpty()) {
            Gallery(photos = spot.photos)
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
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
        modifier = modifier.padding(bottom = 16.dp),
        text = "#${spot.id}",
        fontSize = 11.sp,
        fontWeight = FontWeight.W500,
        color = AppColor.neutralText
    )
    Text(
        modifier = modifier.padding(bottom = 16.dp),
        text = spot.name,
        fontSize = 22.sp,
        fontWeight = FontWeight.W700,
        color = Color.Black
    )

    Text(
        modifier = modifier.padding(bottom = 16.dp),
        text = spot.name,
        fontSize = 14.sp,
        fontWeight = FontWeight(450),
        color = AppColor.neutralText
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

    Column(Modifier.padding(16.dp)) {
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
                0 -> OverviewTab(spot)
                1 -> DetailsTab(spot)
                2 -> ContactTab(spot)
            }
        }
    }
}


@Composable
fun OverviewTab(spot: Spot) {
    Column {
        if (!spot.isPremium) {
            RowContactPremium(spot = spot)
        } else {
            RowContact(spot = spot)
        }
        LocationInfos(spot)
        ItemsList(
            spot.services.subList(0, 3),
            {},
            R.string.view_all_services,
            1,
            title = R.string.services,
            subtitle = R.string.services_subtitle, false
        )
        ItemsList(
            spot.brands.subList(0, 3),
            {},
            R.string.view_all_brands,
            2,
            title = R.string.official_dealers,
            subtitle = R.string.dealers_subtitle, false
        )
        ItemsList(
            spot.services.subList(0, 3),
            {},
            R.string.view_all_services,
            1,
            title = R.string.accessories,
            subtitle = R.string.accessories_subtitle, false
        )
        AppButton(
            isActive = true,
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(top = 50.dp),
            textRes = R.string.navigate
        )
    }
}

@Composable
fun ColumnScope.ItemsList(
    items: List<String>,
    onExtend: () -> Unit,
    @StringRes linkLabel: Int,
    columnCount: Int,
    @StringRes title: Int,
    @StringRes subtitle: Int,
    isFullList: Boolean
) {
    Text(modifier = Modifier.padding(top = 30.dp), text = stringResource(id = title))
    Text(modifier = Modifier.padding(top = 16.dp), text = stringResource(id = subtitle))
    Divider(Modifier.padding(top = 16.dp))
    if (columnCount == 1) {
        items.forEach {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = it,
                color = AppColor.Tertiary,
                fontSize = 14.sp,
                fontWeight = FontWeight(450)
            )
        }
    } else {
        val itemsSplitInHalf =
            items.withIndex().groupBy { it.index / 2 }.map { it.value.map { it1 -> it1.value } }
        val col1 = itemsSplitInHalf[0]
        val col2 = itemsSplitInHalf[1]
        Column(modifier = Modifier.weight(1f)) {
            col1.forEach {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = it,
                    color = AppColor.Tertiary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(450)
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            col2.forEach {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = it,
                    color = AppColor.Tertiary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(450)
                )
            }
        }
    }

    if (isFullList) {
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
        modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 5.dp),
            painter = painterResource(id = R.drawable.distance),
            contentDescription = "",
            tint = AppColor.Primary
        )
        Text(
            text = "30 km${stringResource(id = R.string.from_you)}",
            color = AppColor.Secondary,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp
        )
    }

    Row(
        modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 5.dp),
            painter = painterResource(id = R.drawable.pin_here),
            contentDescription = "",
            tint = AppColor.Primary
        )
        Text(
            text = spot.fullLocation,
            color = AppColor.Secondary,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp
        )
    }

    Row(
        modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 5.dp),
            painter = painterResource(id = R.drawable.my_location),
            contentDescription = "",
            tint = AppColor.Primary
        )
        Text(
            text = spot.fullGeolocalisation,
            color = AppColor.Secondary,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RowContact(spot: Spot) {
    Row {
        if (spot.website.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(15))
                    .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound))
                    .padding(5.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 5.dp),
                    painter = painterResource(id = R.drawable.website),
                    contentDescription = "",
                    tint = AppColor.Primary
                )
                Text(
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
                    .padding(5.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 5.dp),
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = "",
                    tint = AppColor.Primary
                )
                Text(
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
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        if (spot.website.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
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
fun ColumnScope.DetailsTab(spot: Spot) {
    ItemsList(
        spot.services,
        {},
        R.string.view_all_services,
        1,
        title = R.string.services,
        subtitle = R.string.services_subtitle, true
    )
    ItemsList(
        spot.brands,
        {},
        R.string.view_all_brands,
        2,
        title = R.string.official_dealers,
        subtitle = R.string.dealers_subtitle, true
    )
    ItemsList(
        spot.services,
        {},
        R.string.view_all_services,
        1,
        title = R.string.accessories,
        subtitle = R.string.accessories_subtitle, true
    )
}

@Composable
fun ContactTab(spot: Spot) {
    Row {
        Image(painter = painterResource(id = R.drawable.pin_here), contentDescription = "")
        Text(
            modifier = Modifier.padding(start = 22.dp),
            text = spot.fullLocation,
            fontSize = 12.sp,
            fontWeight = FontWeight(450)
        )
    }

    Row {
        Image(painter = painterResource(id = R.drawable.my_location), contentDescription = "")
        Text(
            modifier = Modifier.padding(start = 22.dp),
            text = spot.fullGeolocalisation,
            fontSize = 12.sp,
            fontWeight = FontWeight(450)
        )
    }

    if (spot.website.isNotEmpty()) {
        Row {
            Image(painter = painterResource(id = R.drawable.website), contentDescription = "")
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = spot.website,
                fontSize = 12.sp,
                fontWeight = FontWeight(450)
            )
        }
    }

    if (spot.email.isNotEmpty()) {
        Row {
            Image(painter = painterResource(id = R.drawable.mail), contentDescription = "")
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = spot.email,
                fontSize = 12.sp,
                fontWeight = FontWeight(450)
            )
        }
    }


    if (spot.phone.isNotEmpty()) {
        Row {
            Image(painter = painterResource(id = R.drawable.phone), contentDescription = "")
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = spot.phone,
                fontSize = 12.sp,
                fontWeight = FontWeight(450)
            )
        }
    }



    if (spot.facebook.isNotEmpty()) {
        Row {
            Image(painter = painterResource(id = R.drawable.facebook), contentDescription = "")
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = spot.facebook,
                fontSize = 12.sp,
                fontWeight = FontWeight(450)
            )
        }
    }



    if (spot.twitter.isNotEmpty()) {
        Row {
            Image(painter = painterResource(id = R.drawable.twitter), contentDescription = "")
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = spot.twitter,
                fontSize = 12.sp,
                fontWeight = FontWeight(450)
            )
        }
    }


    if (spot.youtube.isNotEmpty()) {
        Row {
            Image(painter = painterResource(id = R.drawable.youtube), contentDescription = "")
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = spot.youtube,
                fontSize = 12.sp,
                fontWeight = FontWeight(450)
            )
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



