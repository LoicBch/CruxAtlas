package com.appmobiledition.laundryfinder.android.composables

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appmobiledition.laundryfinder.android.R
import com.appmobiledition.laundryfinder.android.ui.theme.AppColor

@Composable
fun PopupParkingLocation(modifier: Modifier, onExit: () -> Unit, onDelete: () -> Unit) {
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
                .align(Alignment.Center)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                IconButton(onClick = { onExit() }) {
                    Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
                }
                Spacer(modifier = Modifier.weight(0.5f))

                Text(
                    text = stringResource(id = R.string.menu_my_location),
                    fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(0.5f))
            }

            Text(
                text = stringResource(id = R.string.delete_location_popup),
                fontWeight = FontWeight(450),
                fontSize = 12.sp,
                color = Color.Black, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                maxLines = 2
            )

            AppButtonSmall(
                onClick = { onDelete() },
                color = AppColor.Red,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp,
                    top = 20.dp
                ),
                drawableRes = R.drawable.trash,
                textRes = R.string.delete
            )
        }
    }
}