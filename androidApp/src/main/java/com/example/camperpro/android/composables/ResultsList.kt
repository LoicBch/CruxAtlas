package com.example.camperpro.android.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.filter.OptionItem
import com.example.camperpro.android.ui.theme.AppColor

@Composable
fun ResultsList(onItemClick: (String) -> Unit, subTitleLabel: String, results: List<String>) {

    val scrollState = rememberScrollState()

    val items = remember {
        mutableStateOf(results)
    }

    Text(
        modifier = Modifier.padding(top = 20.dp),
        text = subTitleLabel,
        color = AppColor.outlineText,
        fontSize = 14.sp,
        fontWeight = FontWeight.W500
    )

    LazyColumn(
        modifier = Modifier
            .padding(top = 22.dp)
            .fillMaxHeight()
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(items.value) {
            OptionItem(Modifier.clickable { onItemClick(it) }, it)
        }
    }
}
