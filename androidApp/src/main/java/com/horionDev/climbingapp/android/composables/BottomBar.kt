package com.horionDev.climbingapp.android.composables

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.horionDev.climbingapp.android.AndroidConstants
import com.horionDev.climbingapp.android.LocalDependencyContainer
import com.horionDev.climbingapp.android.composables.BottomBarDestination.Explore
import com.horionDev.climbingapp.android.destinations.AndroidAppDirectionDestination
import com.horionDev.climbingapp.android.destinations.MainMapDestination
import com.horionDev.climbingapp.android.destinations.MenuScreenDestination
import com.horionDev.climbingapp.android.destinations.NewsFeedScreenDestination
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.android.ui.theme.Dimensions
import com.horionDev.climbingapp.android.R
import com.ramcosta.composedestinations.navigation.navigate
import kotlinx.coroutines.flow.StateFlow

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
                horizontalArrangement = Arrangement.SpaceEvenly
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
                            appViewModel.withNetworkOnly {

                                if (destination.ordinal == Explore.ordinal) {
                                    navController.popBackStack(
                                        MainMapDestination.route, false
                                    )

                                    appViewModel.withGpsOnly {
                                        appViewModel.onEventDisplayedChange(false)
                                        appViewModel.onAroundMeClick()
                                    }
                                    return@withNetworkOnly
                                }

                                if (selected) {
                                    // When we click again on a bottom bar item and it was already selected
                                    // we want to pop the back stack until the initial destination of this bottom bar item
                                    //
                                    //
                                    navController.popBackStack(
                                        MainMapDestination.route, false
                                    )
                                    return@withNetworkOnly
                                }
                                navController.navigate(destination.direction) {

                                    if (destination.ordinal != Explore.ordinal) {
                                        popUpTo(MainMapDestination.route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },

                        icon = {
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
                                    id = shouldDestinationBeSelected(
                                        appViewModel.eventsAreDisplayed,
                                        selected,
                                        destination,
                                        currentBackStack
                                    )
                                ),
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
                                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = getTextColor(
                                    appViewModel.eventsAreDisplayed,
                                    selected,
                                    destination,
                                    currentBackStack
                                )
                            )
                        },
                    )
                }
            }
        }
    }
}

fun getTextColor(
    isEvent: StateFlow<Boolean>,
    selected: Boolean,
    destination: BottomBarDestination,
    currentBackStack: State<NavBackStackEntry?>
): Color {
    Log.d("BOTTOM", isEvent.value.toString())

    return if (isEvent.value) {
        if (destination.direction.route == MenuScreenDestination.route && currentBackStack.value?.destination?.route == MainMapDestination.route) {
            AppColor.Primary
        } else {
            if (destination.direction.route == MainMapDestination.route) {
                AppColor.Tertiary
            } else {
                if (selected) {
                    AppColor.Primary
                } else {
                    AppColor.Tertiary
                }
            }
        }
    } else {
        if (selected) {
            AppColor.Primary
        } else {
            AppColor.Tertiary
        }
    }

}

fun shouldDestinationBeSelected(
    isEvent: StateFlow<Boolean>,
    selected: Boolean,
    destination: BottomBarDestination,
    currentBackStack: State<NavBackStackEntry?>
): Int {
    Log.d("BOTTOM", isEvent.value.toString())
    // TODO: Tranquille.. a refacto

    return if (isEvent.value) {
        if (destination.direction.route == MenuScreenDestination.route && currentBackStack.value?.destination?.route == MainMapDestination.route) {
            destination.iconSelected
        } else {
            if (destination.direction.route == MainMapDestination.route) {
                destination.iconUnselected
            } else {
                if (selected) {
                    destination.iconSelected
                } else {
                    destination.iconUnselected
                }
            }
        }
    } else {
        if (selected) {
            destination.iconSelected
        } else {
            destination.iconUnselected
        }
    }
}

enum class
BottomBarDestination(
    val direction: AndroidAppDirectionDestination,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int,
    @StringRes val label: Int,
    @StringRes val contentDescription: Int
) {

    Home(
        direction = NewsFeedScreenDestination,
        iconSelected = R.drawable.home_selected,
        iconUnselected = R.drawable.home,
        label = R.string.appbar_home,
        contentDescription = R.string.cd_around_me
    ),
    Explore(
        direction = MainMapDestination,
        iconSelected = R.drawable.explore_selected,
        iconUnselected = R.drawable.explore,
        label = R.string.appbar_explore,
        contentDescription = R.string.cd_around_location
    ),

    //    Partners(
    //        direction = AroundLocationScreenDestination,
    //        iconSelected = R.drawable.partners_selected,
    //        iconUnselected = R.drawable.partners,
    //        label = R.string.appbar_partners,
    //        contentDescription = R.string.cd_partners
    //    ),
    Menu(
        direction = MenuScreenDestination,
        iconSelected = R.drawable.menu_selected,
        iconUnselected = R.drawable.menu,
        label = R.string.appbar_menu,
        contentDescription = R.string.cd_menu
    )
}