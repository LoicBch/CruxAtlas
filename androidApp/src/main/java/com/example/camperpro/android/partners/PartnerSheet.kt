package com.example.camperpro.android.partners

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.AppButton

@Composable
fun PartnerSheet() {

    Box {
        Column {
           PhotosList()
            Text(text = "")
            Text(text = "")
            Text(text = "")
            Text(text = "")
            Text(text = "")
            Text(text = "")
            AppButton(isActive = true, onClick = { /*TODO*/ }, modifier = Modifier, textRes = R
                .string.navigate_to_event)
        }

        Row(modifier = Modifier.padding(15.dp)) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(
                    id = R.string.cd_arrow_back
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
        }
    }
}

@Composable
fun PhotosList(){

    val scrollState = rememberScrollState()

    LazyRow(
        modifier = Modifier.scrollable(
            state = scrollState,
            orientation = Orientation.Vertical
        )
    ) {
//            items(spot.photos) {

    }
}