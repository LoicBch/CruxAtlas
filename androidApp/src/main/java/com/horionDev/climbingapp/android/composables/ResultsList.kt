package com.horionDev.climbingapp.android.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.horionDev.climbingapp.android.ui.theme.AppColor

@Composable
fun ResultsList(onItemClick: (String) -> Unit, subTitleLabel: String, results: List<String>) {

    val scrollState = rememberScrollState()

    Text(
        modifier = Modifier.padding(top = 20.dp),
        text = subTitleLabel,
        color = AppColor.Black, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
        fontSize = 14.sp,
        fontWeight = FontWeight.W500
    )

    Divider(modifier = Modifier.padding(top = 5.dp))

    LazyColumn(
        modifier = Modifier
            .padding(top = 22.dp)
            .fillMaxHeight()
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(items = results) {
//            OptionItem(Modifier.clickable { onItemClick(it) }, it)
        }
    }
}
