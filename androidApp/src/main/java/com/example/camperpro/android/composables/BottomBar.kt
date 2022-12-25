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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.camperpro.android.AndroidConstants
import com.example.camperpro.android.NavGraphs
import com.example.camperpro.android.R
import com.example.camperpro.android.destinations.*
import com.example.camperpro.android.ui.theme.Dimensions
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.utils.isRouteOnBackStack

val AndroidAppDestination.shouldShowBottomBar get() = AndroidConstants.ScreensAboveBottomBar.all{ this.route == it.route }

@Composable
fun BottomBar(navController: NavHostController) {

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

//            val appViewModel = LocalDependencyContainer.current.appViewModel


            BottomBarDestination.values().forEach { destination ->
                val selected = navController.isRouteOnBackStack(destination.direction)

                BottomNavigationItem(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    selected = selected,
                    onClick = {

//                        // TODO: find a better solution for this
//                        if (destination.ordinal == 0 && !appViewModel.userClickedAroundMe.value) {
//                            appViewModel.userClickedAroundMe.update { true }
//                        }

                        Log.d("Nav", navController.visibleEntries.toString())

                        if (selected) {
                            Log.d("Nav", "$destination.name is actually displayed")
                        } else {
                            Log.d("Nav", "$destination.name is not displayed")
                            Log.d("Nav", destination.direction.route)
                        }

//                        if (selected && destination.ordinal != 0) {
                        if (selected && destination.ordinal != 0) {
                            // When we click again on a bottom bar item and it was already selected
                            // we want to pop the back stack until the initial destination of this bottom bar item
                            navController.popBackStack(
                                destination.direction, false
                            )
                            return@BottomNavigationItem
                        }


                        navController.navigate(destination.direction) {
                            // Pop up to the root of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            if (destination.ordinal == 0) {

                                popUpTo(NavGraphs.root) {
                                    saveState = true
                                }
                            }

                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },

                    icon = {
                        Image(
                            painter = painterResource(
                                id = if (selected) destination.iconSelected
                                else destination.iconUnselected
                            ),
                            modifier = Modifier.size(33.dp),
                            contentDescription = stringResource(
                                destination.contentDescription
                            ),
                        )
                    },
                    label = {
                        Text(
                            fontSize = 9.sp, text = stringResource(destination.label)
                        )
                    },
                )
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