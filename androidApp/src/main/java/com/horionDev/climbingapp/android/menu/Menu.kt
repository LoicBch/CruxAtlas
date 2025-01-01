package com.horionDev.climbingapp.android.menu

//import com.horionDev.climbingapp.android.destinations.AddSpotLocationDestination
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.LocalDependencyContainer
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.destinations.MyAssetsScreenDestination
import com.horionDev.climbingapp.android.destinations.SettingsMenuDestination
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.android.ui.theme.Dimensions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

data class MenuItem(
    @StringRes val labelRes: Int,
    val onclick: () -> Unit,
    @DrawableRes val drawableRes: Int,
    @StringRes val contentDescriptionRes: Int,
    val isSubMenu: Boolean
)


// TODO:  Mettre en place un systeme d'event avec un consumer pour le resultNavigator
@Destination
@Composable
fun MenuScreen(
    navigator: DestinationsNavigator, bookmarksResult: ResultBackNavigator<Boolean>
) {

    val menuItems: List<MenuItem> = listOf(
//        MenuItem(R.string.menu_events, {
//            resultNavigator.navigateBack(result = true, true)
//        }, R.drawable.events, R.string.cd_events, false), MenuItem(
//            R.string.menu_travel_checklists, {
//
//            }, R.drawable.checklist, R.string.cd_travel_checklist, true
//        ),
//        MenuItem(
//            R.string.add_location, {
//                navigator.navigate(AddSpotTypeScreenDestination())
//            }, R.drawable.my_location, R.string.cd_my_location, false
//        ),
        MenuItem(
            R.string.menu_account, {
                navigator.navigate(MyAssetsScreenDestination)
            }, R.drawable.download, R.string.dowloaded, false
        ),
        MenuItem(
            R.string.bookmarks, {
                bookmarksResult.setResult(true)
            }, R.drawable.bookmark, R.string.sectors, false
        ),
        MenuItem(
            R.string.menu_app_settings, {
                navigator.navigate(SettingsMenuDestination)
            }, R.drawable.settings, R.string.sectors, false
        )
    )

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
                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.CenterVertically),
                color = AppColor.Primary
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        )

        menuItems.filter { !it.isSubMenu }.forEach { menuItem ->
            MenuItem(menuItem = menuItem)

//            if (menuItem.labelRes == R.string.menu_events) {

//                Divider(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 15.dp)
//                )
//
//                Dropdown(
//                    title = stringResource(id = R.string.menu_travel_tools),
//                    modifier = Modifier.padding(horizontal = 15.dp)
//                ) {
//                    Column {
//                        MenuItem(menuItem = menuItems.find { it.labelRes == R.string.menu_travel_checklists }!!)
//                        MenuItem(menuItem = menuItems.find { it.labelRes == R.string.menu_my_location }!!)
//                    }
//                }
//            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            )
        }
    }
}

@Composable
fun MenuItem(
    menuItem: MenuItem
) {

    val appViewModel = LocalDependencyContainer.current.appViewModel

    Row(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(
                top = 15.dp,
                bottom = 15.dp,
                end = 15.dp,
                start = if (menuItem.isSubMenu) 30.dp else 0.dp
            )
            .clickable {
                menuItem.onclick()
            }, verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .size(24.dp),
            painter = painterResource(id = menuItem.drawableRes),
            contentDescription = stringResource(id = menuItem.contentDescriptionRes),
            tint = AppColor.Tertiary
        )

        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(id = menuItem.labelRes),
            fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight.W700,
            fontSize = 16.sp,
            color = AppColor.Tertiary
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Sharp.ArrowForward,
            contentDescription = "",
            tint = AppColor.Secondary
        )
    }
}
//
//@Composable
//fun PubContainerMenu(pub: MenuLink) {
//
//    val uriHandler = LocalUriHandler.current
//
//    Row(
//        Modifier
//            .fillMaxWidth()
//            .height(70.dp)
//            .padding(start = 16.dp, end = 15.dp)
//            .clickable {
//                uriHandler.openUri(pub.url)
//                uriHandler.openUri(pub.urlstat)
//            }, verticalAlignment = Alignment.CenterVertically
//    ) {
//        GlideImage(
//            modifier = Modifier.size(30.dp), imageModel = { pub.icon }, imageOptions = ImageOptions(
//                contentScale = ContentScale.FillBounds, alignment = Alignment.Center
//            )
//        )
//        Column(
//            modifier = Modifier.padding(start = 16.dp), verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = pub.name,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.W700,
//                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
//                color = AppColor.Tertiary
//            )
//            Text(
//                text = pub.subtitle,
//                fontSize = 11.sp,
//                fontWeight = FontWeight.W500,
//                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
//                color = AppColor.Tertiary
//            )
//        }
//        Spacer(modifier = Modifier.weight(1f))
//
//        Icon(
//            imageVector = Icons.Sharp.ArrowForward,
//            contentDescription = "",
//            tint = AppColor.Secondary
//        )
//
//    }
//}