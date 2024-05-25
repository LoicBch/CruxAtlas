package com.appmobiledition.laundryfinder.android.spotSheet
import android.app.Application
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.appmobiledition.laundryfinder.android.R
import com.appmobiledition.laundryfinder.android.composables.AppButton
import com.appmobiledition.laundryfinder.android.extensions.*
import com.appmobiledition.laundryfinder.android.ui.theme.AppColor
import com.appmobiledition.laundryfinder.android.ui.theme.Dimensions
import com.appmobiledition.laundryfinder.data.model.dto.LaundryDto
import com.appmobiledition.laundryfinder.data.model.dto.PhotoDto
import com.appmobiledition.laundryfinder.domain.model.Dealer
import com.appmobiledition.laundryfinder.domain.model.Laundry
import com.appmobiledition.laundryfinder.domain.model.Photo
import com.appmobiledition.laundryfinder.domain.model.composition.Location
import com.appmobiledition.laundryfinder.domain.model.composition.distanceFromUserLocationText
import com.appmobiledition.laundryfinder.utils.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Destination
@Composable
fun LaundryDetailScreen(navigator: DestinationsNavigator, laundry: LaundryDto) {

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(bottom = 80.dp)
                .fillMaxWidth()
        ) {
            Header(laundry, onClose = { navigator.popBackStack() }, onHelp = {
                //                navigator.navigate(
                //                    HelpScreenDestination
                //                )
            })
            BaseInfos(Modifier.padding(horizontal = 16.dp), laundry)
            Tabs(laundry)
        }

        Column {
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.background(Color.White)) {
                AppButton(
                    isActive = true,
                    onClick = {
                        context.navigateByGmaps(
                            context,
                            laundry.latitude!!.toDouble(),
                            laundry.longitude!!.toDouble()
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    textRes = R.string.navigate
                )
            }
        }
    }

}

@Composable
fun Header(dealer: LaundryDto, onClose: () -> Unit, onHelp: () -> Unit) {

    val context = LocalContext.current
    val shareContent =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Box(
        Modifier
//            .height(if (dealer.photos.isNotEmpty()) 250.dp else 100.dp)
            .fillMaxWidth()
    ) {

//        if (dealer.photos.isNotEmpty()) {
        val photos = listOf(PhotoDto(1.toString(), "", "", "", "", ""),PhotoDto(1.toString(), "", "", "", "", ""))
            Gallery(photos)
//        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 30.dp)
            ) {

                Image(
                    modifier = Modifier.clickable { onClose() },
                    painter = painterResource(id = R.drawable.cross_round),
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    modifier = Modifier.clickable { context.share("", shareContent) },
                    painter = painterResource(id = R.drawable.share_round),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun Gallery(photos: List<PhotoDto>) {

    val listState = rememberLazyListState()
    val needToReposition by remember {
        derivedStateOf {
            !listState.isScrollInProgress && listState.firstVisibleItemScrollOffset != 0
        }
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        state = listState
    ) {
        itemsIndexed(photos) { _, photo ->
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .background(color = AppColor.Tertiary)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                GlideImage(
                    modifier = Modifier
                        .height(250.dp),
                    //                        .fillParentMaxWidth(),
                    imageModel = { R.drawable.vatertag },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillBounds, alignment = Alignment.Center
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    when (listState.isScrollingUp()) {
        true -> {
            LaunchedEffect(needToReposition) {
                if (needToReposition) {
                    listState.animateScrollToItem(listState.firstVisibleItemIndex)
                }
            }
        }
        false -> {
            LaunchedEffect(needToReposition) {
                if (needToReposition) {
                    if (listState.firstVisibleItemScrollOffset > 400) {
                        listState.animateScrollToItem(listState.lastVisibleItemIndex!!)
                    } else {
                        listState.animateScrollToItem(listState.firstVisibleItemIndex)
                    }
                }
            }
        }
    }
}

@Composable
fun BaseInfos(modifier: Modifier, dealer: LaundryDto) {
    Text(
        modifier = modifier.padding(bottom = 12.dp),
        text = "#${dealer.id}",
        fontSize = 11.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
        fontWeight = FontWeight.W500,
        color = Color.Black
    )
    Text(
        modifier = modifier.padding(bottom = 16.dp),
        text = dealer.name!!,
        fontSize = 22.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
        fontWeight = FontWeight.W700,
        color = Color.Black
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(dealer: LaundryDto) {
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
                            fontWeight = FontWeight.W500,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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
                0 -> ContactTab(dealer)
            }
        }
    }
}

@Composable
fun LocationInfos(dealer: Dealer) {

    val application = LocalContext.current.applicationContext as Application

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
            text = Location(dealer.latitude, dealer.longitude).distanceFromUserLocationText(
                KMMPreference(application)
            ),
            color = Color.Black,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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
            text = dealer.fullLocation,
            color = Color.Black,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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
            text = dealer.fullGeolocalisation,
            color = Color.Black,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight.W500,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RowContact(dealer: Dealer) {

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Row(modifier = Modifier.padding(top = 20.dp)) {
        if (dealer.website.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(15))
                    .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound))
                    .padding(vertical = 5.dp, horizontal = 12.dp)
                    .clickable { uriHandler.openUri(dealer.website) },
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
                    color = AppColor.Secondary,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }

        if (dealer.phone.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(15))
                    .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound))
                    .padding(vertical = 5.dp, horizontal = 12.dp)
                    .clickable { context.dial(dealer.phone) },
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
                    color = AppColor.Secondary,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp
                )
            }
        }

    }
}

@Composable
fun RowContactPremium(dealer: Dealer) {

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 20.dp)
    ) {
        if (dealer.website.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { uriHandler.openUri(dealer.website) }) {
                Icon(
                    painter = painterResource(id = R.drawable.website),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }

        if (dealer.phone.isNotEmpty()) {
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

        if (dealer.email.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = {
                           context.sendMail(
                               dealer.email, ""
                           )
                       }) {
                Icon(
                    painter = painterResource(id = R.drawable.mail),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }

        if (dealer.facebook.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { uriHandler.openUri(dealer.facebook) }) {
                Icon(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }

        if (dealer.twitter.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { uriHandler.openUri(dealer.twitter) }) {
                Icon(
                    painter = painterResource(id = R.drawable.twitter),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }

        if (dealer.youtube.isNotEmpty()) {
            IconButton(modifier = Modifier
                .background(
                    color = Color.White, RoundedCornerShape(Dimensions.radiusRound)
                )
                .padding(end = 12.dp)
                .border(1.dp, AppColor.Secondary, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { uriHandler.openUri(dealer.youtube) }) {
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
fun ContactTab(dealer: LaundryDto) {

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxHeight()) {

        Text(text = dealer.name?:"" ,fontSize = 20.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
             fontWeight = FontWeight(450),modifier = Modifier.padding(top = 30.dp,start = 16.dp))

        Text(text = dealer.description?:"", fontSize = 16.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
             fontWeight = FontWeight(450),modifier = Modifier.padding(top = 16.dp,start = 16.dp))

        Row(
            modifier = Modifier.padding(vertical = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.pin_here),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = dealer.fullLocation,
                fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontWeight = FontWeight(450)
            )
        }
        Divider()

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.my_location),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = dealer.fullGeolocalisation,
                fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontWeight = FontWeight(450)
            )
        }
        Divider()

//        if (dealer.site_internet!!.isNotEmpty()) {
//            Row(
//                modifier = Modifier
//                    .padding(vertical = 16.dp)
//                    .clickable { uriHandler.openUri(dealer.site_internet!!) },
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    modifier = Modifier.size(20.dp),
//                    painter = painterResource(id = R.drawable.website),
//                    contentDescription = ""
//                )
//                Text(
//                    modifier = Modifier.padding(start = 22.dp),
//                    text = dealer.site_internet!!,
//                    fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
//                    fontWeight = FontWeight(450)
//                )
//            }
//            Divider()
//        }

//        if (dealer.mail!!.isNotEmpty()) {
//            Row(
//                modifier = Modifier
//                    .padding(vertical = 16.dp)
//                    .clickable { context.sendMail(dealer.mail!!, "") },
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    modifier = Modifier.size(20.dp),
//                    painter = painterResource(id = R.drawable.mail),
//                    contentDescription = ""
//                )
//                Text(
//                    modifier = Modifier.padding(start = 22.dp),
//                    text = dealer.mail!!,
//                    fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
//                    fontWeight = FontWeight(450)
//                )
//            }
//            Divider()
//        }
//
//
//        if (dealer.tel!!.isNotEmpty()) {
//            Row(
//                modifier = Modifier
//                    .padding(vertical = 16.dp)
//                    .clickable { context.dial(dealer.tel!!) },
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    modifier = Modifier.size(20.dp),
//                    painter = painterResource(id = R.drawable.phone),
//                    contentDescription = ""
//                )
//                Text(
//                    modifier = Modifier.padding(start = 22.dp),
//                    text = dealer.tel!!,
//                    fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
//                    fontWeight = FontWeight(450)
//                )
//            }
//            Divider()
//        }
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



