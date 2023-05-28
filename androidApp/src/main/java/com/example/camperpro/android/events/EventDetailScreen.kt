package com.example.camperpro.android.events

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.AppButton
import com.example.camperpro.android.extensions.navigateByGmaps
import com.example.camperpro.android.extensions.share
import com.example.camperpro.android.spotSheet.Gallery
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.Event
import com.example.camperpro.domain.model.Photo
import com.example.camperpro.utils.fullGeolocalisation
import com.example.camperpro.utils.fullLocation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Destination
@Composable
fun EventDetailScreen(navigator: DestinationsNavigator, event: Event) {
    Column {
        Header(event, onClose = { navigator.popBackStack() })
        Infos(Modifier.padding(horizontal = 16.dp), event)
    }
}

@Composable
fun Header(event: Event, onClose: () -> Unit) {

    val context = LocalContext.current
    val shareContent = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Box(Modifier.heightIn(min = 50.dp)) {

        //        if (dealer.isPremium && dealer.photos.isNotEmpty()) {
        Gallery(photos = listOf())
        //        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 30.dp)
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
    }
}

@Composable
fun GalleryEvent(photos: List<Photo>) {
    val scrollState = rememberScrollState()

    LazyRow {
        itemsIndexed(photos) { index, photo ->
            GlideImage(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                imageModel = { photo.url },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop
                )
            )
        }
    }
}

@Composable
fun Infos(modifier: Modifier, event: Event) {

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Text(
        modifier = modifier.padding(bottom = 12.dp),
        text = "#${event.id}",
        fontSize = 11.sp,
        fontWeight = FontWeight.W500,
        color = Color.Black
    )
    Text(
        modifier = modifier.padding(bottom = 16.dp),
        text = event.name,
        fontSize = 22.sp,
        fontWeight = FontWeight.W700,
        color = Color.Black
    )

    Text(
        modifier = modifier.padding(bottom = 16.dp),
        text = event.descriptionEn,
        fontSize = 14.sp,
        fontWeight = FontWeight(450),
        color = Color.Black
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.pin_here),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(start = 22.dp),
                text = event.fullLocation,
                fontSize = 12.sp,
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
                text = event.fullGeolocalisation,
                fontSize = 12.sp,
                fontWeight = FontWeight(450)
            )
        }
        Divider()

        if (event.website.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { uriHandler.openUri(event.website) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.website),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 22.dp),
                    text = event.website,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(450)
                )
            }
            Divider()
        }

        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            isActive = true,
            onClick = {
                context.navigateByGmaps(context, event.latitude, event.longitude)
            },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            textRes = R.string.navigate
        )
    }
}
