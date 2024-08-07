//package com.horionDev.climbingapp.android.addSpot
//
//import android.icu.text.Transliterator.Position
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight.Companion.Bold
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.horion.murbex.android.R
//import com.horion.murbex.android.composables.SmallAppButton
//import com.horion.murbex.android.destinations.AddSpotPhotoScreenDestination
//import com.horion.murbex.android.ui.theme.Dimensions
//import com.horion.murbex.domain.model.Itinerary
//import com.horion.murbex.domain.model.Location
//import com.horion.murbex.domain.model.Spot
//import com.ramcosta.composedestinations.annotation.Destination
//import com.ramcosta.composedestinations.navigation.DestinationsNavigator
//import com.ramcosta.composedestinations.navigation.popUpTo
//import com.ramcosta.composedestinations.result.ResultBackNavigator
//
//@Destination
//@Composable
//fun AddSpotThankYouScreen(navigator: DestinationsNavigator, spot: Spot, resultNavigator: ResultBackNavigator<Location>) {
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.radialGradient(
//                    colors = listOf(Color(0xFFefd006), Color(0xFFffffff)),
//                )
//            ), contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = "Thank you for your participation !",
//                modifier = Modifier.padding(10.dp),
//                fontSize = Dimensions.bigTitle,
//                fontWeight = Bold,
//                textAlign = TextAlign.Center
//            )
//
//            Image(
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .size(500.dp)
//                    .padding(vertical = 50.dp),
////                painter = painterResource(id = R.drawable.logo_clear),
//                painter = painterResource(id = R.drawable.logo),
//                contentDescription = ""
//            )
//
//            Row(modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 30.dp)) {
//                Spacer(modifier = Modifier.weight(1f))
//
//                SmallAppButton(
//                    isActive = true,
//                    onClick = {
//                        resultNavigator.navigateBack(Location(spot.latitude, spot.longitude))
//                    },
//                    modifier = Modifier.padding(10.dp),
//                    textRes = R.string.see_my_spot
//                )
//
//                SmallAppButton(
//                    isActive = true,
//                    onClick = {
//                        navigator.navigate(AddSpotPhotoScreenDestination(spot)) {
//                            popUpTo(AddSpotPhotoScreenDestination)
//                        }
//                    },
//                    modifier = Modifier.padding(10.dp),
//                    textRes = R.string.add_photos
//                )
//
//                Spacer(modifier = Modifier.weight(1f))
//            }
//            Spacer(modifier = Modifier.weight(1f))
//        }
//    }
//}