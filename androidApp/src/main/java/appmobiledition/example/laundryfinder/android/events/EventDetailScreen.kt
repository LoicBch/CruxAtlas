package com.example.camperpro.android.events

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.AppButton
import com.example.camperpro.android.destinations.HelpScreenDestination
import com.example.camperpro.android.extensions.navigateByGmaps
import com.example.camperpro.android.extensions.share
import com.example.camperpro.android.spotSheet.Gallery
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
        Header(event, onClose = { navigator.popBackStack() }, onHelp = {
            navigator.navigate(
                HelpScreenDestination
            )
        })
        Infos(Modifier.padding(horizontal = 16.dp), event)
    }
}

@Composable
fun Header(event: Event, onClose: () -> Unit, onHelp: () -> Unit) {

    val context = LocalContext.current
    val shareContent =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Box(
        Modifier
            .height(if (event.photos.isNotEmpty()) 250.dp else 100.dp)
            .fillMaxWidth()
    ) {

        if (event.photos.isNotEmpty()) {
            Gallery(photos = listOf())
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 30.dp)
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

            Image(
                modifier = Modifier.clickable { onHelp() },
                painter = painterResource(id = R.drawable.help_round),
                contentDescription = ""
            )

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
        fontSize = 11.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
        fontWeight = FontWeight.W500,
        color = Color.Black
    )
    Text(
        modifier = modifier.padding(bottom = 16.dp),
        text = event.name,
        fontSize = 22.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
        fontWeight = FontWeight.W700,
        color = Color.Black
    )

    Text(
        modifier = modifier.padding(bottom = 16.dp),
        text = event.descriptionEn,
        fontSize = 14.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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
                text = event.fullGeolocalisation,
                fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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
                    fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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
