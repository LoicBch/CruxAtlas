package com.example.camperpro.android.partners

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination

data class Partner(val title: String)

@Destination
@Composable
fun PartnersScreen(partners: List<Partner>) {

    val scrollState = rememberScrollState()

    LazyColumn(
        modifier = Modifier
            .padding(top = 15.dp)
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(partners.size) {

        }
    }
}