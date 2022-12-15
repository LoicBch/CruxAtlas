package com.example.camperpro.android.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material.icons.sharp.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.camperpro.android.R
import com.example.camperpro.android.ui.theme.Dimensions
import com.ramcosta.composedestinations.annotation.Destination
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

enum class MenuItems(val item: @Composable () -> Unit) {
    TRAVEL_TOOLS({
        MenuItem(
            label = stringResource(id = R.string.menu_travel_tools),
            onclick = { /*TODO*/ },
            menuIcon = {
                Image(modifier = Modifier.size(30.dp),
                    imageVector = Icons.Sharp.Star, contentDescription = stringResource(
                        id = R
                            .string.cd_travel_tools
                    )
                )
            }, trailingIcon = null
        )
    }),
    TRAVEL_CHECKLIST({
        MenuItem(
            label = stringResource(id = R.string.menu_travel_checklists),
            onclick = { /*TODO*/ },
            menuIcon = {
                Image(modifier = Modifier.size(30.dp),
                    imageVector = Icons.Sharp.Star,
                    contentDescription = stringResource(id = R.string.cd_travel_checklist)
                )
            }, trailingIcon = null
        )
    }),
    LEVELER({
        MenuItem(label = stringResource(id = R.string.menu_leveler),
            onclick = { /*TODO*/ },
            menuIcon = {
                Image(modifier = Modifier.size(30.dp),
                    imageVector = Icons.Sharp.Star,
                    contentDescription = stringResource(id = R.string.cd_leveler)
                )
            }, trailingIcon = null
        )
    }),
    MY_LOCATION({
        MenuItem(label = stringResource(id = R.string.menu_my_location),
            onclick = { /*TODO*/ },
            menuIcon = {
                Image(modifier = Modifier.size(30.dp),
                    imageVector = Icons.Sharp.Star,
                    contentDescription = stringResource(id = R.string.cd_my_location)
                )
            }, trailingIcon = null
        )
    }),
    APP_SETTINGS({
        MenuItem(label = stringResource(id = R.string.menu_app_settings),
            onclick = { /*TODO*/ },
            menuIcon = {
                Image(modifier = Modifier.size(50.dp),
                    imageVector = Icons.Sharp.Settings,
                    contentDescription = stringResource(id = R.string.cd_app_settings)
                )
            }, trailingIcon = null
        )
    })

}

@Destination
@Composable
fun MenuScreen() {
    Column(
        Modifier
            .background(Color.White)
            .fillMaxWidth()
    ) {

        Row(
            Modifier
                .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 40.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.menu_selected),
                contentDescription = stringResource(id = R.string.around_location_placeholder)
            )

            Text(
                text = stringResource(id = R.string.appbar_menu),
                fontWeight = FontWeight.Black,
                fontSize = Dimensions.bigTitle,
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        MenuItems.values().forEach {
            it.item()
            Divider(modifier = Modifier.fillMaxWidth())
        }

        PubContainer("")
        Divider(modifier = Modifier.fillMaxWidth())
        PubContainer("")

    }
}

@Composable

fun MenuItem(
    label: String, onclick: () -> Unit, menuIcon: @Composable () -> Unit, trailingIcon:
    @Composable() (() -> Unit?)?
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(vertical = 15.dp)
    ) {
        IconButton(onClick = onclick) {
            menuIcon()
        }
        Text(modifier = Modifier.align(Alignment.CenterVertically), text = label)
        Spacer(modifier = Modifier.weight(1f))
        trailingIcon?.let {
            IconButton(onClick = onclick) {
                trailingIcon()
            }
        }
    }
}

@Composable
fun PubContainer(url: String) {
    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Yellow)
            .padding(vertical = 25.dp),
        imageModel = { url }, // loading a network image using an URL.
        imageOptions = ImageOptions(
            contentScale = ContentScale.FillBounds, alignment = Alignment.Center
        )
    )
}