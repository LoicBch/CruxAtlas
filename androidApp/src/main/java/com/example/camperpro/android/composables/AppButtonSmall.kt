package com.example.camperpro.android.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.ui.theme.Dimensions

@Composable
fun AppButtonSmall(
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier,
    @DrawableRes drawableRes: Int,
    @StringRes textRes: Int
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimensions.buttonSmall)
            .clip(RoundedCornerShape(Dimensions.radiusAppButton)),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color
        )
    ) {
        Row {
            Icon(painterResource(drawableRes), contentDescription = "", tint = Color.White)
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 15.dp),
                text = stringResource(
                    id = textRes
                ),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.W700
            )
        }
    }
}