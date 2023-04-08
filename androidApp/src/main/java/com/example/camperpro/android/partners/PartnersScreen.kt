package com.example.camperpro.android.partners

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.collectAsStateWithLifecycleImmutable
import com.example.camperpro.android.destinations.PartnerDetailsScreenDestination
import com.example.camperpro.domain.model.Partner
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun PartnersScreen(
    navigator: DestinationsNavigator, viewModel: PartnersViewModel = getViewModel()
) {

    val partners by viewModel.partners.collectAsStateWithLifecycleImmutable()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadPartners()
    }

    LazyColumn(
        modifier = Modifier
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
            .background(Color.White)
            .fillMaxSize()
            .padding(top = 30.dp),
    ) {
        items(partners.value) { item ->
            Row(modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, bottom = 20.dp, top = 5.dp)
                .shadow(2.dp, RoundedCornerShape(8))
                .zIndex(1f)
                .fillMaxWidth()
                .height(130.dp)
                .background(Color.White, RoundedCornerShape(8))
                .clickable {
                    navigator.navigate(PartnerDetailsScreenDestination(partner = item))
                }) {
                VerticalListItem(item)
            }
        }
    }
}


@Composable
fun VerticalListItem(partner: Partner) {

    if (partner.photos.isNotEmpty()) {
        Box {
            GlideImage(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp, bottomStart = 8.dp
                        )
                    )
                    .size(130.dp),
                imageModel = { partner.photos[0].url },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillHeight, alignment = Alignment.Center
                )
            )
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            fontSize = 14.sp,
            text = partner.name
        )

        Text(
            fontWeight = FontWeight(450),
            maxLines = 2,
            fontSize = 10.sp,
            text = partner.description
        )

        Spacer(modifier = Modifier.weight(1f))

        Row {
            Image(
                painter = painterResource(id = R.drawable.shield),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 5.dp)
                    .shadow(2.dp, RoundedCornerShape(15))
                    .zIndex(1f)
                    .background(Color.White, RoundedCornerShape(15))
                    .padding(5.dp)
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
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}