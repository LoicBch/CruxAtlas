package com.horionDev.climbingapp.android.spotSheet

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.extensions.*
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.data.model.dto.LaundryDto
import com.horionDev.climbingapp.data.model.dto.PhotoDto
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Sector
import com.horionDev.climbingapp.domain.model.entities.ceuse
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Destination
@Composable
fun CragSheet(navigator: DestinationsNavigator, crag: Crag) {

    val photos = remember {
        mutableStateListOf<PhotoDto>()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Header(crag, onClose = { navigator.popBackStack() }, onHelp = {
                //                navigator.navigate(
                //                    HelpScreenDestination
                //                )
            }, photos = photos)
            BaseInfos(Modifier.padding(horizontal = 16.dp), crag)
            Tabs(crag, navigator) {
                photos.apply {
                    clear()
                    addAll(
                        listOf(
                            PhotoDto(
                                1.toString(),
                                it.image,
                                "",
                                "",
                                "",
                                ""
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Header(
    crag: Crag,
    onClose: () -> Unit,
    onHelp: () -> Unit,
    photos: SnapshotStateList<PhotoDto>
) {

    val context = LocalContext.current
    val shareContent =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Box(
        Modifier
            //            .height(if (dealer.photos.isNotEmpty()) 250.dp else 100.dp)
            .fillMaxWidth()
    ) {

        //        if (dealer.photos.isNotEmpty()) {

        LaunchedEffect(true) {
            photos.addAll(
                listOf(
                    PhotoDto(
                        1.toString(),
                        "https://image.thecrag.com/1x117:2321x1335/fit-in/1200x630/e9/9f/e99f6b3a38e59c5fc88cde5fa4b870eb1473a550",
                        "",
                        "",
                        "",
                        ""
                    ),
                    PhotoDto(
                        1.toString(),
                        "https://image.thecrag.com/1x117:2321x1335/fit-in/1200x630/e9/9f/e99f6b3a38e59c5fc88cde5fa4b870eb1473a550",
                        "",
                        "",
                        "",
                        ""
                    )
                )
            )
        }

        Gallery(photos)
        //        }

        Column(modifier = Modifier.fillMaxWidth()) {
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
                    imageModel = { photo.link_large },
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
fun BaseInfos(modifier: Modifier, crag: Crag) {
    Text(
        modifier = modifier.padding(vertical = 16.dp),
        text = crag.name,
        fontSize = 22.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
        fontWeight = FontWeight.W700,
        color = Color.Black
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(crag: Crag, navigator: DestinationsNavigator,  onSectorSelected: (Sector) -> Unit) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val tabScreensTitle = listOf(
        stringResource(id = R.string.general),
        stringResource(id = R.string.approach),
        stringResource(id = R.string.topos)
    )

    val density = LocalDensity.current

    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabScreensTitle.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
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
                            fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                            color = if (pagerState.currentPage == index) AppColor.Primary else AppColor.Tertiary
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.scrollToPage(index) } },
                )
            }
        }

        HorizontalPager(
            modifier = Modifier.fillMaxHeight(),
            count = tabScreensTitle.size,
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> GeneralTab(crag = crag, navigator = navigator)
                1 -> ApproachTab(crag = crag)
                2 -> TopoTab(crag = crag, onSectorSelected = {
                    onSectorSelected(it)
                })
            }
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
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing), label = ""
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing), label = ""
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .background(AppColor.Primary)
        .height(2.dp)
        .width(currentTabWidth)
}



