package com.example.camperpro.android.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.material.icons.sharp.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.Dropdown
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.MenuLink
import com.example.camperpro.utils.Globals
import com.ramcosta.composedestinations.annotation.Destination
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

enum class MenuItems(val item: @Composable () -> Unit) {
    TRAVEL_CHECKLIST({
                         MenuItem(
                             label = stringResource(id = R.string.menu_travel_checklists),
                             onclick = { /*TODO*/ },
                             menuIcon = {
                                 Image(
                                     modifier = Modifier
                                         .size(30.dp)
                                         .padding(start = 16.dp),
                                     painter = painterResource(id = R.drawable.checklist),
                                     contentDescription = stringResource(id = R.string.cd_travel_checklist)
                                 )
                             }, trailingIcon = {
                                 androidx.compose.material.Icon(
                                     imageVector = Icons.Sharp.ArrowForward,
                                     contentDescription = "",
                                     tint = AppColor.Secondary
                                 )
                             }, isSubMenu = true
                         )
                     }),
    LEVELER({
                MenuItem(
                    label = stringResource(id = R.string.menu_leveler),
                    onclick = { /*TODO*/ },
                    menuIcon = {
                        Image(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Sharp.Star,
                            contentDescription = stringResource(id = R.string.cd_leveler)
                        )
                    }, trailingIcon = null
                )
            }),
    MY_LOCATION({
                    MenuItem(
                        label = stringResource(id = R.string.menu_my_location),
                        onclick = { /*TODO*/ },
                        menuIcon = {
                            Image(
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 16.dp),
                                painter = painterResource(id = R.drawable.my_location),
                                contentDescription = stringResource(id = R.string.cd_my_location)
                            )
                        }, trailingIcon = {
                            androidx.compose.material.Icon(
                                imageVector = Icons.Sharp.ArrowForward,
                                contentDescription = "",
                                tint = AppColor.Secondary
                            )
                        }, isSubMenu = true
                    )
                }),
    APP_SETTINGS({
                     MenuItem(
                         label = stringResource(id = R.string.menu_app_settings),
                         onclick = { /*TODO*/ },
                         menuIcon = {
                             Image(
                                 modifier = Modifier.size(50.dp),
                                 painter = painterResource(id = R.drawable.settings),
                                 contentDescription = stringResource(id = R.string.cd_app_settings)
                             )
                         }, trailingIcon = {
                             androidx.compose.material.Icon(
                                 imageVector = Icons.Sharp.ArrowForward,
                                 contentDescription = "", tint = AppColor.Secondary
                             )
                         }
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
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.menu_selected),
                contentDescription = stringResource(id = R.string.around_location_placeholder),
                colorFilter = ColorFilter.tint(color = AppColor.Primary)
            )

            Text(
                text = stringResource(id = R.string.appbar_menu),
                fontWeight = FontWeight.Black,
                fontSize = Dimensions.bigTitle,
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.CenterVertically),
                color = AppColor.Primary
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Dropdown(
            title = stringResource(id = R.string.menu_travel_tools),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Column() {
                MenuItems.TRAVEL_CHECKLIST.item.invoke()
                MenuItems.MY_LOCATION.item.invoke()
            }
        }

        MenuItems.values().toMutableList().subList(3, MenuItems.values().size).forEach {
            it.item()
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        Globals.menuLinks.forEach {
            PubContainerMenu(it)
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun MenuItem(
    label: String, onclick: () -> Unit, menuIcon: @Composable () -> Unit, trailingIcon:
    @Composable (() -> Unit?)?, isSubMenu: Boolean = false
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(vertical = 15.dp)
    ) {
        IconButton(onClick = onclick) {
            menuIcon()
        }
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = label,
            fontWeight = if (isSubMenu) FontWeight.W500 else FontWeight.W700,
            fontSize = if (isSubMenu) 14.sp else 16.sp,
            color = AppColor.Tertiary
        )
        Spacer(modifier = Modifier.weight(1f))
        trailingIcon?.let {
            IconButton(onClick = onclick) {
                trailingIcon()
            }
        }
    }
}

@Composable
fun PubContainerMenu(pub: MenuLink) {

    val uriHandler = LocalUriHandler.current

    Row(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(start = 16.dp)
            .clickable {
                uriHandler.openUri(pub.url)
                uriHandler.openUri(pub.urlstat)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            modifier = Modifier.size(30.dp),
            imageModel = { pub.icon },
            imageOptions = ImageOptions(
                contentScale = ContentScale.FillBounds, alignment = Alignment.Center
            )
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = pub.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.W700,
                color = AppColor.Tertiary
            )
            Text(
                text = pub.subtitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.W500,
                color = AppColor.Tertiary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Sharp.ArrowForward,
                contentDescription = "",
                tint = AppColor.Secondary
            )
        }
    }
}