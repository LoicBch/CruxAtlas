package com.horionDev.climbingapp.android.areaDetails

import CragDetailsDto
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.cragsDetails.BaseInfos
import com.horionDev.climbingapp.android.cragsDetails.Gallery
import com.horionDev.climbingapp.android.cragsDetails.Tabs
import com.horionDev.climbingapp.android.cragsDetails.customTabIndicator
import com.horionDev.climbingapp.android.destinations.LoginScreenDestination
import com.horionDev.climbingapp.android.extensions.isScrollingUp
import com.horionDev.climbingapp.android.extensions.lastVisibleItemIndex
import com.horionDev.climbingapp.android.extensions.share
import com.horionDev.climbingapp.android.parcelable.AreaParcel
import com.horionDev.climbingapp.android.parcelable.fromParcelable
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.data.model.dto.PhotoDto
import com.horionDev.climbingapp.domain.model.entities.Area
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Sector
import com.horionDev.climbingapp.utils.SessionManager
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.util.Collections.addAll

@Destination
@Composable
fun AreaDetails(
    navigator: DestinationsNavigator,
    areaParcel: AreaParcel,
    viewModel: AreaDetailsViewModel = getViewModel()
) {
    LaunchedEffect(true) {
        viewModel.init(areaParcel.fromParcelable())
    }

    val photos by viewModel.photos.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val area by viewModel.area.collectAsState()
    val crags by viewModel.cragsFromArea.collectAsState()


    val pm = LocalContext.current.packageManager
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
            }
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Header(
                area = area.fromParcelable(),
                onClose = { navigator.popBackStack() },
                onHelp = {},
                photos = photos
            )
            BaseInfos(Modifier.padding(horizontal = 16.dp), area = area.fromParcelable())
            Tabs(
                area = area.fromParcelable(),
                crags = crags,
                navigator,
                isFavorite,
                onFavoriteClick = {
                    if (SessionManager.isLogged()) {
                        viewModel.permuteFavorite(area.id.toString())
                    } else {
                        navigator.navigate(LoginScreenDestination)
                    }
                },
                onNavigateClick = {
                    val gmmIntentUri = Uri.parse("")
//                        Uri.parse("google.navigation:q=${area.latitude},${area.longitude}")
                    val wazeUri = Uri.parse("")
//                        Uri.parse("waze://?ll=${area.latitude},${area.longitude}&navigate=yes")

                    val gmmPackage = "com.google.android.apps.maps"
                    val wazePackage = "com.waze"


                    val isGoogleMapsInstalled = isAppInstalled(gmmPackage, pm)
                    val isWazeInstalled = isAppInstalled(wazePackage, pm)

                    val intent = if (isGoogleMapsInstalled) {
                        Intent(Intent.ACTION_VIEW, gmmIntentUri).apply { `package` = gmmPackage }
                    } else if (isWazeInstalled) {
                        Intent(Intent.ACTION_VIEW, wazeUri).apply { `package` = wazePackage }
                    } else {
                        Intent(Intent.ACTION_VIEW, wazeUri).apply { `package` = wazePackage }
                    }
                    launcher.launch(intent)
                },
                onView3DClick = {
                    // TODO
                })
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

    if (photos.isEmpty()) {
        photos.toMutableList().addAll(
            listOf(
                PhotoDto(
                    "https://media.istockphoto.com/id/1441026821/vector/no-picture-available-placeholder-thumbnail-icon-illustration.jpg?s=612x612&w=0&k=20&c=7K9T9bguFyJyKOTvPkdoTWZYRWA3zGvx_xQI53BT0wg="
                )
            )
        )
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
                    .background(color = AppColor.PlaceHolderImage)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                GlideImage(
                    modifier = Modifier
                        .height(250.dp),
                    imageModel = { photo.url },
                    imageOptions = ImageOptions(
//                        contentScale = ContentScale.FillBounds, alignment = Alignment.Center
                        contentScale = ContentScale.FillHeight, alignment = Alignment.Center
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
fun BaseInfos(modifier: Modifier, area: Area) {
    Text(
        modifier = modifier.padding(vertical = 16.dp),
        text = area.name,
        fontSize = 22.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
        fontWeight = FontWeight.W700,
        color = Color.Black
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(
    area: Area,
    crags: List<Crag>,
    navigator: DestinationsNavigator,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onNavigateClick: () -> Unit,
    onView3DClick: () -> Unit
) {
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
            modifier = Modifier
                .fillMaxHeight(),
            count = tabScreensTitle.size,
            state = pagerState,
        ) { page ->
            when (page) {
//                0 -> GeneralTab(crag = crag, navigator = navigator)
                0 -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    InfosTab(area = area, navigator = navigator, isFavorite,
                        onFavoriteClick = {
                            onFavoriteClick()
                        }, onNavigateClick = {
                            onNavigateClick()
                        },
                        onView3DClick = {
                            onView3DClick()
                        }
                    )
                }

                1 -> MapTab(crags = crags, area = area, navigator = navigator)
                2 -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CragsTab(crags = crags)
                }
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

@Composable
fun Header(
    area: Area,
    onClose: () -> Unit,
    onHelp: () -> Unit,
    photos: List<PhotoDto>
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

            photos.toMutableList().addAll(
                listOf(
                    PhotoDto(
                        "https://media.istockphoto.com/id/1441026821/vector/no-picture-available-placeholder-thumbnail-icon-illustration.jpg?s=612x612&w=0&k=20&c=7K9T9bguFyJyKOTvPkdoTWZYRWA3zGvx_xQI53BT0wg=",
                    )
                )
            )
//                    ,
//                    PhotoDto(
//                        "https://image.thecrag.com/1x117:2321x1335/fit-in/1200x630/e9/9f/e99f6b3a38e59c5fc88cde5fa4b870eb1473a550"
//                    )
//                )
//            )
        }



        Gallery(photos)

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

            Spacer(modifier = Modifier.height(100.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 30.dp)
            ) {

                Image(
                    modifier = Modifier
                        .clickable { onClose() }
                        .alpha(0f),
                    painter = painterResource(id = R.drawable.cross_round),
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(25),
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(25)
                        ), onClick = {

                    }) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(end = 5.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = ""
                        )
                        Text(text = "Add")
                    }
                }
            }
        }
    }
}

private fun isAppInstalled(packageName: String, pm: PackageManager): Boolean {
    return try {
        pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}