package com.appmobiledition.laundryfinder.android.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appmobiledition.laundryfinder.android.R
import com.appmobiledition.laundryfinder.android.ui.theme.AppColor
import com.appmobiledition.laundryfinder.android.ui.theme.Dimensions

@Composable
fun LoadingModal(modifier: Modifier) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.ClearGrey)
    ) {
        Column(
            modifier
                .padding(horizontal = 34.dp)
                .shadow(4.dp, RoundedCornerShape(Dimensions.radiusTextField))
                .background(
                    color = Color.White, shape = RoundedCornerShape(10.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
                .width(IntrinsicSize.Max)
        ) {

            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.loading),
                    fontWeight = FontWeight(450),
                    color = Color.Black,
                    fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                    maxLines = 1
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(20.dp),
                    color = AppColor.Primary, strokeWidth = 2.dp
                )
            }
        }
    }
}