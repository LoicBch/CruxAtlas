package com.horionDev.climbingapp.android.addSpot

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.destinations.AddSpotPhotoScreenDestination
import com.horionDev.climbingapp.android.login.TextFieldAnimate
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddSpotInformationsScreen(
    navigator: DestinationsNavigator,
    crag: Crag
    //    viewModel: AddSpotInformationViewModel = getViewModel()
) {

    var altitude by remember {
        mutableStateOf("")
    }

    var orientation by remember {
        mutableStateOf("")
    }

    var approachLength by remember {
        mutableStateOf("")
    }

    //    LaunchedEffect(key1 = true) {
    //        viewModel.event.collect {
    //            when (it) {
    //               is AddSpotInformationViewModel.AddSpotInfoEvent.NavigateToThanksYou -> navigator.navigate(
    //                    AddSpotThankYouScreenDestination(
    //                        it.spot
    //                    ), builder = {
    //                        popUpTo(MainMapDestination)
    //                    }
    //                )
    //            }
    //        }
    //    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Add informations",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(modifier = Modifier.alpha(0f), onClick = { }) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
            }
        }

        LazyVerticalGrid(
            modifier = Modifier.weight(1f), columns = GridCells.Fixed(2)
        ) {

            item(span = { GridItemSpan(2) }) {
                AltitudeSection(currentAltitude = crag.altitude, onAltitudeChange = {
                    altitude = it
                })
            }

            item(span = { GridItemSpan(2) }) {
                OrientationSection(currentOrientation = crag.orientation, onChange = {
                    orientation = it
                })
            }

            item(span = { GridItemSpan(2) }) {
                ApproachSection(currentApproach = crag.approachLenght, onChange = {
                    approachLength = it
                })
            }
        }

        AppButton(
            isActive = true,
            onClick = {
                navigator.navigate(AddSpotPhotoScreenDestination(crag))
                //                viewModel.addSpot(
                //                    spot.copy(
                //                        name = name,
                //                        description = description,
                //                        creationDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
                //                    )
                //                )
            },
            modifier = Modifier.padding(vertical = 18.dp),
            textRes = com.horionDev.climbingapp.R.string.next
        )
    }
}

@Composable
fun AltitudeSection(
    currentAltitude: String?,
    onAltitudeChange: (String) -> Unit
) {
    Column {
        Text(text = "Altitude", fontWeight = FontWeight.Bold)

        Divider(modifier = Modifier.padding(vertical = 10.dp))

        TextFieldAnimate( initialContent = currentAltitude,
            modifier = Modifier.padding(top = 10.dp), placeHolder = R.string.search
        ) { onAltitudeChange(it.text) }
    }
}

@Composable
fun OrientationSection(
    currentOrientation: String?,
    onChange: (String) -> Unit
) {
    Column {
        Text(text = "Orientation", fontWeight = FontWeight.Bold)

        Divider(modifier = Modifier.padding(vertical = 10.dp))

        TextFieldAnimate(initialContent = currentOrientation,
            modifier = Modifier.padding(top = 10.dp), placeHolder = R.string.search
        ) { onChange(it.text) }
    }
}

//@Composable
//fun OrientationSection(
//    onChange: (String) -> Unit
//) {
//    Column {
//        Text(text = "Altitude", fontWeight = FontWeight.Bold)
//
//        Divider(modifier = Modifier.padding(vertical = 10.dp))
//
//        TextFieldAnimate(
//            modifier = Modifier.padding(top = 10.dp), placeHolder = R.string.search
//        ) { onChange(it.text) }
//    }
//}

@Composable
fun ApproachSection(
    currentApproach: String?,
    onChange: (String) -> Unit
) {
    Column {
        Text(text = "Approach length", fontWeight = FontWeight.Bold)

        Divider(modifier = Modifier.padding(vertical = 10.dp))

        TextFieldAnimate( initialContent = currentApproach,
            modifier = Modifier.padding(top = 10.dp), placeHolder = R.string.search
        ) { onChange(it.text) }
    }
}

//@Composable
//fun ApproachLengthSection(
//    modifier: Modifier = Modifier,
//    description: String,
//    onChange: (String) -> Unit
//) {
//    Column(Modifier.padding(top = 10.dp)) {
//        Text(text = "Description", fontWeight = FontWeight.Bold)
//
//        Divider(modifier = Modifier.padding(vertical = 10.dp))
//
//        OutlinedTextField(placeholder = { Text(text = "") },
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(top = 10.dp)
//                .height(100.dp),
//            value = description,
//            onValueChange = {
//                onChange(it)
//            },
//        )
//    }
//}


//@Composable
//fun <T : Enum<T>> InformationItem(
//    isSelected: Boolean,
//    onSelect: () -> Unit,
//    type: Enum<T>
//) {
//    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
//        IconButton(onClick = { onSelect() }) {
//            Icon(
//                painter = painterResource(id = R.drawable.around_place),
//                contentDescription = "",
//                tint = if (!isSelected) Color.Gray else Color.Red
//            )
//        }
//
//        Text(text = type.name, fontWeight = FontWeight.Bold, fontSize = Dimensions.smallTitle)
//        Text(
//            textAlign = TextAlign.Center,
//            text = getInformationSubtitle(type),
//            fontWeight = FontWeight.Medium
//        )
//    }
//}

//inline fun <T> getInformationSubtitle(type: T): String {
//    return when (type) {
//        is SpotCondition -> {
//            when (type) {
//                SpotCondition.RUINS -> "(place is in ruins, structure is damaged, no interior...)"
//                SpotCondition.EMPTY -> "(place is empty but structure is intact..)"
//                SpotCondition.GOOD -> "(place is in good condition, some furniture are still there...)"
//                SpotCondition.CLEAN -> "(place is clean, furniture are present,no trash, no graffiti...)"
//            }
//        }
//        is SpotVicinity -> when (type) {
//            SpotVicinity.RURAL -> "(place is in the middle of nowhere, no house around...)"
//            SpotVicinity.SUBURB -> "(place is in a suburb, some houses around...)"
//            SpotVicinity.CITY -> "(place is in a city, a lot of houses around...)"
//        }
//        is SpotProtection -> when (type) {
//            SpotProtection.FENCE -> "(place is protected by a fence...)"
//            SpotProtection.CAMERA -> "(place is protected by a camera...)"
//            SpotProtection.NONE -> "(No protection)"
//            SpotProtection.GUARDIAN -> "(place is protected by a guardian...)"
//            SpotProtection.ALARM -> "(place is protected by an alarm...)"
//        }
//        else -> ""
//    }
//}