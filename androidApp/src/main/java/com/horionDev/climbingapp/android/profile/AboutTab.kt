package com.horionDev.climbingapp.android.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R

@Composable
fun AboutTab(Gallery: @Composable () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
//            .verticalScroll(rememberScrollState())
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

        Text(text = "Gender: Male")
        Text(text = "Country: France")
        Text(text = "Age: 25")
        Text(text = "Height: 1.80m")
        Text(text = "Weight: 70kg")
        Text(text = "Climbing since 2015")
        Text(text = "Member since 2023")

        Gallery()
    }
}