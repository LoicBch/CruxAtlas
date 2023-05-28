package com.example.camperpro.android.partners

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
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
import com.example.camperpro.android.extensions.sendMail
import com.example.camperpro.android.extensions.share
import com.example.camperpro.android.spotSheet.*
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.Partner
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun PartnerDetailsScreen(navigator: DestinationsNavigator, partner: Partner) {

    Column {
        Header(partner = partner, onClose = { navigator.popBackStack() })
        BasePartnerInfos(Modifier.padding(horizontal = 16.dp), partner)
        Tabs(partner = partner)
    }
}

@Composable
fun Header(partner: Partner, onClose: () -> Unit) {

    val context = LocalContext.current
    val shareContent = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Box(Modifier.heightIn(min = 50.dp)) {

        if (partner.isPremium && partner.photos.isNotEmpty()) {
            Gallery(photos = partner.photos)
        }

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
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
                    .padding(end = 5.dp)
                    .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
                    .zIndex(1f)
                    .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
                           onClick = { context.share("", shareContent) }) {
                    Icon(painter = painterResource(id = R.drawable.share), contentDescription = "")
                }

                IconButton(modifier = Modifier
                    .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
                    .zIndex(1f)
                    .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
                           onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.help), contentDescription = "")
                }
            }

            Row(
                modifier = Modifier.padding(bottom = 30.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 5.dp),
                        painter = painterResource(id = R.drawable.shield),
                        contentDescription = "",
                        tint = AppColor.Primary
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
                    Image(
                        modifier = Modifier.padding(end = 5.dp),
                        painter = painterResource(id = R.drawable.premium_badge),
                        contentDescription = ""
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
fun BasePartnerInfos(modifier: Modifier, partner: Partner) {
    Text(
        modifier = modifier.padding(bottom = 12.dp),
        text = "#${partner.id}",
        fontSize = 11.sp,
        fontWeight = FontWeight.W500,
        color = Color.Black
    )

    Text(
        modifier = modifier.padding(bottom = 12.dp),
        text = partner.name,
        fontSize = 22.sp,
        fontWeight = FontWeight.W700,
        color = Color.Black
    )

    Text(
        modifier = modifier.padding(bottom = 24.dp),
        text = partner.description,
        fontSize = 14.sp,
        fontWeight = FontWeight(450),
        color = Color.Black
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(partner: Partner) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val tabScreensTitle = listOf(
        stringResource(id = R.string.overview),
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
                0 -> OverviewTab(partner, pagerState)
                1 -> ContactTab(partner)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OverviewTab(partner: Partner, pagerState: PagerState) {

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {

        RowContactPremium(partner = partner)

        Text(modifier = Modifier.padding(top = 20.dp),
            text = "Here is space for a brief description of the offer. It can be a short introduction to an insurance stating for whom it is, it could be an intro into the rental options, etc. To learn more visit website...",
            fontSize = 14.sp,
            fontWeight = FontWeight(450),
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            isActive = true,
            onClick = { },
            modifier = Modifier.padding(bottom = 30.dp),
            textRes = R.string.get_offer
        )
    }
}

@Composable
fun RowContactPremium(partner: Partner) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
    ) {
        if (partner.website.isNotEmpty()) {
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

        if (partner.phone.isNotEmpty()) {
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

        if (partner.email.isNotEmpty()) {
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

        if (partner.facebook.isNotEmpty()) {
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

        if (partner.twitter.isNotEmpty()) {
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

        if (partner.youtube.isNotEmpty()) {
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
fun ContactTab(partner: Partner) {

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxHeight()) {

        if (partner.website.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { uriHandler.openUri(partner.website) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.website),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = partner.website,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }

        if (partner.email.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { context.sendMail(partner.email, "") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.mail),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = partner.email,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }


        if (partner.phone.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { context.dial(partner.phone) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = partner.phone,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }



        if (partner.facebook.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { uriHandler.openUri(partner.facebook) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = partner.facebook,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }



        if (partner.twitter.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { uriHandler.openUri(partner.twitter) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.twitter),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = partner.twitter,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }


        if (partner.youtube.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { uriHandler.openUri(partner.youtube) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.youtube),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = partner.youtube,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }
    }
}


@Composable
fun PhotosList() {

    val scrollState = rememberScrollState()

    LazyRow(
        modifier = Modifier.scrollable(
            state = scrollState, orientation = Orientation.Vertical
        )
    ) { //            items(spot.photos) {

    }
}