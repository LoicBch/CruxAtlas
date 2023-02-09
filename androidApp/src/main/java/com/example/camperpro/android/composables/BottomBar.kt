package com.example.camperpro.android.composables

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.camperpro.android.AndroidConstants
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.R
import com.example.camperpro.android.destinations.*
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.ramcosta.composedestinations.navigation.navigate

val NavBackStackEntry.shouldShowBottomBar get() = AndroidConstants.ScreensOverBottomBar.all { this.destination.route != it.route }

@Composable
fun BottomBar(navController: NavHostController) {
    val currentBackStack = navController.currentBackStackEntryAsState()

    if (currentBackStack.value != null && currentBackStack.value?.shouldShowBottomBar!!) {
        Surface(
            color = Color.White,
            contentColor = contentColorFor(backgroundColor = Color.White),
            elevation = BottomNavigationDefaults.Elevation
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(Dimensions.bottomBarHeight)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomBarDestination.values().forEach { destination ->
                    val selected =
                        currentBackStack.value?.destination?.route == destination.direction.route
                    val appViewModel = LocalDependencyContainer.current.appViewModel

                    navController.backQueue.forEach {
                        it.destination.route?.let { it1 -> Log.d("Nav", it1) }
                    }


                    BottomNavigationItem(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        selected = selected,
                        onClick = {
                            if (destination.ordinal == 0) {
                                navController.popBackStack(
                                    MainMapDestination.route, false
                                )
                                appViewModel.onAroundMeClick()
                            }

                            if (selected && destination.ordinal != 0) {
                                // When we click again on a bottom bar item and it was already selected
                                // we want to pop the back stack until the initial destination of this bottom bar item
                                navController.popBackStack(
                                    MainMapDestination.route, false
                                )
                                return@BottomNavigationItem
                            }


                            navController.navigate(destination.direction) {

                                if (destination.ordinal != 0) {
                                    popUpTo(MainMapDestination.route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },

                        icon = {
                            // TODO: can be optimised
                            if (selected) {
                                Image(
                                    modifier = Modifier
                                        .offset(y = (-12).dp)
                                        .width(30.dp)
                                        .height(4.dp),
                                    painter = painterResource(id = R.drawable.bottom_bar_indicator),
                                    contentDescription = ""
                                )
                            }

                            Image(
                                painter = painterResource(
                                    id = if (selected) destination.iconSelected
                                    else destination.iconUnselected
                                ),
                                modifier = Modifier.size(30.dp),
                                contentDescription = stringResource(
                                    destination.contentDescription
                                ),
                            )
                        },
                        label = {
                            Text(
                                fontSize = 9.sp,
                                text = stringResource(destination.label),
                                fontWeight = FontWeight.W700,
                                color = if (selected) AppColor.Primary else AppColor.Tertiary
                            )
                        },
                    )
                }
            }
        }
    }
}

enum class BottomBarDestination(
    val direction: AndroidAppDirectionDestination,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int,
    @StringRes val label: Int,
    @StringRes val contentDescription: Int
) {

    AroundMe(
        direction = MainMapDestination,
        iconSelected = R.drawable.around_me_selected,
        iconUnselected = R.drawable.around_me,
        label = R.string.appbar_around_me,
        contentDescription = R.string.cd_around_me
    ),
    AroundLocation(
        direction = AroundLocationScreenDestination,
        iconSelected = R.drawable.around_location_selected,
        iconUnselected = R.drawable.around_location,
        label = R.string.appbar_around_location,
        contentDescription = R.string.cd_around_location
    ),
    Partners(
        direction = PartnersScreenDestination,
        iconSelected = R.drawable.partners_selected,
        iconUnselected = R.drawable.partners,
        label = R.string.appbar_partners,
        contentDescription = R.string.cd_partners
    ),
    Menu(
        direction = MenuScreenDestination,
        iconSelected = R.drawable.menu_selected,
        iconUnselected = R.drawable.menu,
        label = R.string.appbar_menu,
        contentDescription = R.string.cd_menu
    )
}