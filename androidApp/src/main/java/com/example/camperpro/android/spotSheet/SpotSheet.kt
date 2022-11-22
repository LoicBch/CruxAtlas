package com.example.camperpro.android.spotSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Call
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.material.icons.sharp.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.camperpro.android.ui.theme.AppSubtitle
import com.example.camperpro.android.ui.theme.AppText
import com.example.camperpro.android.ui.theme.AppTitle
import com.example.camperpro.android.ui.theme.Yellow
import com.example.camperpro.domain.model.Spot
import com.ramcosta.composedestinations.annotation.Destination
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Destination
@Composable
fun SpotSheet(spot: Spot) {

    val scrollState = rememberScrollState()

    Column {
        LazyRow(
            modifier = Modifier.scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            )
        ) {
            items(spot.photos) {
                GlideImage(
                    imageModel = { it.url }, // loading a network image using an URL.
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                )
            }
        }

        Column(modifier = Modifier.padding(15.dp)) {
            Text(modifier = Modifier.padding(top = 5.dp), text = spot.name, style = AppTitle)
            Text(modifier = Modifier.padding(top = 5.dp), text = spot.lat.toString(), style = AppSubtitle)
            Text(modifier = Modifier.padding(top = 5.dp), text = spot.long.toString(), style = AppSubtitle)
            Text(modifier = Modifier.padding(top = 5.dp), text = spot.description, style = AppText)
        }

        Row(modifier = Modifier.fillMaxSize()) {
            IconButton(modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
                .clip(RoundedCornerShape(100))
                .background(Yellow), onClick = { /*TODO*/ }) {
                Icon(Icons.Sharp.Star, "Favorite")
            }

            IconButton(modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(100))
                .weight(1f)
                .background(Color.Gray), onClick = { /*TODO*/ }) {
                Icon(Icons.Sharp.Call, "Call")
            }

            IconButton(modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
                .clip(RoundedCornerShape(100))
                .background(Color.Gray), onClick = { /*TODO*/ }) {
                Icon(Icons.Sharp.PlayArrow, "Itinerary")
            }

            IconButton(modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
                .clip(RoundedCornerShape(100))
                .background(Color.Gray), onClick = { /*TODO*/ }) {
                Icon(Icons.Sharp.Add, "Add")
            }
        }
    }


}

