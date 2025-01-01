package com.horionDev.climbingapp.android.profile

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.domain.model.entities.User

@Composable
fun AboutTab(user: User, Gallery: @Composable () -> Unit) {

    val gender by remember(user.gender) { derivedStateOf { user.gender.ifEmpty { "Unknown" } } }
    val country by remember(user.country) { derivedStateOf { user.country.ifEmpty { "Unknown" } } }
    val city by remember(user.city) { derivedStateOf { user.city.ifEmpty { "Unknown" } } }
    val age by remember(user.age) { derivedStateOf { if (user.age == 0) "Unknown" else user.age.toString() } }
    val weight by remember(user.weight) { derivedStateOf { if (user.weight == 0) "Unknown" else "${user.weight} kg" } }
    val height by remember(user.height) { derivedStateOf { if (user.height == 0) "Unknown" else "${user.height} cm" } }
    val climbingSince by remember(user.climbingSince) { derivedStateOf { user.climbingSince.ifEmpty { "Unknown" } } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .scrollable(
                state = rememberScrollState(),
                orientation = Orientation.Vertical
            )
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Informations", fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(
                    Font(R.font.oppinsedium)
                )
            )
        }
        Divider(modifier = Modifier.padding(bottom = 12.dp))

        Text(text = "Gender : $gender", fontFamily = FontFamily(Font(R.font.oppinsedium)))
        Text(text = "Country : $country", fontFamily = FontFamily(Font(R.font.oppinsedium)))
        Text(text = "City : $city", fontFamily = FontFamily(Font(R.font.oppinsedium)))
        Text(text = "Age : $age", fontFamily = FontFamily(Font(R.font.oppinsedium)))
        Text(text = "Height : $height", fontFamily = FontFamily(Font(R.font.oppinsedium)))
        Text(text = "Weight : $weight", fontFamily = FontFamily(Font(R.font.oppinsedium)))
        Text(text = "Climbing since $climbingSince", fontFamily = FontFamily(Font(R.font.oppinsedium)))

        Gallery()
    }
}