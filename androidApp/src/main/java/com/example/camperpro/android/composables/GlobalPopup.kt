package com.example.camperpro.android.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.example.camperpro.android.ui.theme.AppColor


@Composable
fun GlobalPopup(modifier: Modifier, state: GlobalPopupState, onExit: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.ClearGrey)
    ) {
        Column(
            modifier
                .padding(horizontal = 34.dp)
                .shadow(4.dp, RoundedCornerShape(15))
                .background(
                    color = Color.White, shape = RoundedCornerShape(15)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
                .align(Alignment.Center) //            .width(IntrinsicSize.Min)
        ) {

            Text(
                text = when (state) {
                    GlobalPopupState.NETWORK_MISSING -> stringResource(id = R.string.popup_missing_network_title)
                    GlobalPopupState.GPS_MISSING -> stringResource(id = R.string.popup_missing_gps_title)
                    GlobalPopupState.HID -> ""
                }, fontWeight = FontWeight.W700, fontSize = 22.sp, color = Color.Black, maxLines = 1
            )
            Text(
                text = when (state) {
                    GlobalPopupState.NETWORK_MISSING -> stringResource(id = R.string.popup_missing_network_text)
                    GlobalPopupState.GPS_MISSING -> stringResource(id = R.string.popup_missing_gps_text)
                    GlobalPopupState.HID -> ""
                }, fontWeight = FontWeight(450), fontSize = 16.sp, color = Color.Black, maxLines = 1
            )
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onExit() }) {
                    Icon(imageVector = Icons.Sharp.Close, contentDescription = "")
                }
            }
        }

    }
}

enum class GlobalPopupState {
    HID, NETWORK_MISSING, GPS_MISSING
}
