package com.horionDev.climbingapp.android.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.login.LoginScreenViewModel
import com.horionDev.climbingapp.android.login.TextFieldAnimate
import com.horionDev.climbingapp.android.spotSheet.ApproachTab
import com.horionDev.climbingapp.android.spotSheet.GeneralTab
import com.horionDev.climbingapp.android.spotSheet.TopoTab
import com.horionDev.climbingapp.android.spotSheet.customTabIndicator
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.domain.model.entities.BoulderLog
import com.horionDev.climbingapp.domain.model.entities.RouteLog
import com.horionDev.climbingapp.utils.SessionManager
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator, viewModel: ProfileViewModel = getViewModel()
) {

    val popupState by viewModel.galleryPopup.collectAsState()
    val routeLogs by viewModel.routeLogs.collectAsState()
    val boulderLogs by viewModel.boulderLogs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        ProfileBlock()
        Tabs(routeLogs, boulderLogs) {
            viewModel.showPopup(it)
        }
    }

    if (popupState != ProfileViewModel.GalleryPopup.HID) {
        PopupGallery(onClose = {
            viewModel.hidePopup()
        }, url = popupState.url)
    }
}

@Composable
fun ProfileBlock() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(25),
            )
            .background(
                color = Color.White, shape = RoundedCornerShape(25)
            )
            .padding(16.dp)
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                imageModel = { SessionManager.user.imageUrl ?: R.drawable.default_profile },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds, alignment = Alignment.Center
                )
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = SessionManager.user.username,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium))
            )
        }

        Column(
            Modifier
                .fillMaxHeight()
                .padding(start = 16.dp)
                .weight(2f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(Modifier) {
                Text(
                    text = "Ascents : 155",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )
            }

            Row(Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Red point : 8A",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )
            }

            Row(Modifier) {
                Text(
                    text = "Flashed : 7b+",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(
    routeLogs: List<RouteLog>, boulderLogs: List<BoulderLog>, onImageClick: (String) -> Unit
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val tabScreensTitle = listOf(
        stringResource(id = R.string.about),
        stringResource(id = R.string.routes),
        stringResource(id = R.string.boulders)
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
            modifier = Modifier.fillMaxHeight(), count = tabScreensTitle.size, state = pagerState
        ) { page ->
            when (page) {
                0 -> AboutTab {
                    ProfileGallery(
                        urls = listOf(
                            "https://plus.unsplash.com/premium_photo-1664474619075-644dd191935f?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aW1hZ2V8ZW58MHx8MHx8fDA%3D",
                            "https://fps.cdnpk.net/images/home/subhome-ai.webp?w=649&h=649",
                            "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                            "https://letsenhance.io/static/03620c83508fc72c6d2b218c7e304ba5/11499/UpscalerAfter.jpg",
                            "https://pixlr.com/images/generator/text-to-image.webp",
                            "https://gratisography.com/wp-content/uploads/2024/03/gratisography-vr-glasses-800x525.jpg",
                            "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                            "https://letsenhance.io/static/03620c83508fc72c6d2b218c7e304ba5/11499/UpscalerAfter.jpg",
                            "https://pixlr.com/images/generator/text-to-image.webp"
                        )
                    ) {
                        onImageClick(it)
                    }
                }

                1 -> RoutesTab(routeLogs)
                2 -> BouldersTab(boulderLogs)
            }
        }
    }
}

@Composable
fun ProfileGallery(urls: List<String>, onImageClick: (String) -> Unit) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Gallery",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.oppinsedium))
            )
        }

        Divider()

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(urls) { url ->
                GlideImage(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        onImageClick(url)
                    }, imageModel = { url }, imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit
                )
                )
            }
        }
    }
}

@Composable
fun PopupGallery(onClose: () -> Unit, url: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.ClearGrey)
            .clickable {
                onClose()
            }) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                imageModel = { url },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit
                )
            )
        }
    }
}