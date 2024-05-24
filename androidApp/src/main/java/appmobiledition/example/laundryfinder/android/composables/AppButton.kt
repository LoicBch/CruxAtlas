package com.example.camperpro.android.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions

@Composable
fun AppButton(isActive: Boolean, onClick: () -> Unit, modifier: Modifier, @StringRes textRes: Int) {

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimensions.buttonHeight)
            .clip(RoundedCornerShape(Dimensions.radiusAppButton)),
        onClick = onClick,
        enabled = isActive,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isActive) AppColor.Secondary
            else AppColor.unSelectedFilterOption
        )
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(
                id = textRes,
            ),
            textAlign = TextAlign.Center, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.W700
        )
    }
}