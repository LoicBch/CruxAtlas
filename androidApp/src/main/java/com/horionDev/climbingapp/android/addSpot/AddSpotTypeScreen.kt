package com.horionDev.climbingapp.android.addSpot

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.horionDev.climbingapp.android.LocalDependencyContainer
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.composables.GlobalPopupState
import com.horionDev.climbingapp.android.destinations.AddSpotLocationScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddSpotTypeScreen(navigator: DestinationsNavigator) {

    val appViewModel = LocalDependencyContainer.current.appViewModel
    val types = listOf("Area", "Crag", "Route", "Boulder")
    var typeSelectedIndex by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Create spot",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(modifier = Modifier.alpha(0f), onClick = { }) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
            }
        }

        Text(
            modifier = Modifier.padding(top = 18.dp),
            text = "What do you want to add ?",
            fontWeight = FontWeight.Black
        )

        Divider(modifier = Modifier.padding(vertical = 10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TypeItem(
                selected = typeSelectedIndex == 0,
                type = "Area",
                subtitle = "",
                icon = R.drawable.area,
                modifier = Modifier
                    .height(150.dp)
                    .weight(1f),
                onShowInfo = {
                    val popup = GlobalPopupState.CUSTOM
                    popup.title = "Area"
                    popup.content =
                        "An area is a place where you can find multiple crags, routes or boulders."
                    appViewModel.showGlobalPopup(popup)
                }, onSelect = {
                    typeSelectedIndex = 0
                }
            )
            Spacer(modifier = Modifier.weight(0.1f))
            TypeItem(
                selected = typeSelectedIndex == 1,
                type = "Crag", subtitle = "", icon = R.drawable.crag, modifier = Modifier
                    .height(150.dp)
                    .weight(1f), onShowInfo = {
                    val popup = GlobalPopupState.CUSTOM
                    popup.title = "Area"
                    popup.content =
                        "An area is a place where you can find multiple crags, routes or boulders."
                    appViewModel.showGlobalPopup(popup)
                }, onSelect = {
                    typeSelectedIndex = 1
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            TypeItem(
                selected = typeSelectedIndex == 2,
                type = "Route", subtitle = "", icon = R.drawable.routes, modifier = Modifier
                    .height(150.dp)
                    .weight(1f), onShowInfo = {
                    val popup = GlobalPopupState.CUSTOM
                    popup.title = "Area"
                    popup.content =
                        "An area is a place where you can find multiple crags, routes or boulders."
                    appViewModel.showGlobalPopup(popup)
                }, onSelect = {
                    typeSelectedIndex = 2
                }
            )
            Spacer(modifier = Modifier.weight(0.1f))
            TypeItem(
                selected = typeSelectedIndex == 3,
                type = "Boulder", subtitle = "", icon = R.drawable.bouldering, modifier = Modifier
                    .height(150.dp)
                    .weight(1f), onShowInfo = {
                    val popup = GlobalPopupState.CUSTOM
                    popup.title = "Area"
                    popup.content =
                        "An area is a place where you can find multiple crags, routes or boulders."
                    appViewModel.showGlobalPopup(popup)
                }, onSelect = {
                    typeSelectedIndex = 3
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            isActive = typeSelectedIndex != -1,
            onClick = {
                when (typeSelectedIndex) {
                    0 -> navigator.navigate("addSpotArea")
                    1 -> navigator.navigate(AddSpotLocationScreenDestination)
                    2 -> navigator.navigate("addSpotRoute")
                    3 -> navigator.navigate("addSpotBoulder")
                }
            },
            modifier = Modifier.padding(bottom = 18.dp),
            textRes = com.horionDev.climbingapp.R.string.next
        )
    }
}

@Composable
fun TypeItem(
    selected: Boolean,
    type: String,
    subtitle: String,
    icon: Int,
    modifier: Modifier,
    onSelect: () -> Unit,
    onShowInfo: (String) -> Unit
) {
    Box(modifier = modifier
        .border(
            BorderStroke(2.dp, if (selected) Color.Blue else Color.Gray),
            shape = RoundedCornerShape(15)
        )
        .clickable {
            onSelect()
        }) {
        Row(modifier = modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onShowInfo(type) }) {
                Icon(painterResource(id = R.drawable.question), contentDescription = "", tint = if (selected) Color.Blue else Color.Unspecified)
            }
        }
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(id = icon),
                contentDescription = "",
                tint = if (selected) Color.Blue else Color.Unspecified
            )
            Text(
                text = type,
                color = if (selected) Color.Blue else Color.Unspecified,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

//@Composable
//fun typeItem(
//    isSelected: Boolean,
//    onSelect: () -> Unit
//) {
//    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
//
//        val matrix = ColorMatrix()
//        matrix.setToSaturation(0F)
//
//        IconButton(onClick = { onSelect() }) {
//            Image(
//                painter = painterResource(id = type.poi()),
//                contentDescription = "",
//                colorFilter = if (isSelected) null else ColorFilter.colorMatrix(matrix)
//            )
//        }
//
//        Text(text = type.title, fontWeight = FontWeight.Bold)
//        Text(
//            text = "test",
//            fontWeight = FontWeight.Medium,
//            textAlign = TextAlign.Center
//        )
//    }
//}