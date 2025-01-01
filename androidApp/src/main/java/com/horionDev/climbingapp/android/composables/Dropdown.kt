package com.horionDev.climbingapp.android.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.ui.theme.AppColor

@Composable
fun Dropdown(modifier: Modifier, title: String, content: @Composable () -> Unit) {
    var isExpanded by remember { mutableStateOf(true) }
    val angle: Float by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 360f,
        animationSpec = tween(
            durationMillis = 500, easing = LinearEasing
        )
    )
    Column {
        Row(
            Modifier
                .height(50.dp)
                .clickable { isExpanded = !isExpanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Icon(
//                modifier = Modifier
//                    .padding(start = 16.dp),
//                painter = painterResource(id = R.drawable.tools),
//                contentDescription = stringResource(
//                    id = R.string.dowloaded
//                ),
//                tint = if (isExpanded) AppColor.Primary else AppColor.Tertiary
//            )

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = title,
                fontWeight = FontWeight.W700,
                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontSize = 16.sp,
                color = if (isExpanded) AppColor.Primary else AppColor.Tertiary
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.arrow_dropdown),
                tint = if (isExpanded) AppColor.Primary else AppColor.Secondary,
                contentDescription = "Open or close the drop down",
                modifier = Modifier
                    .padding(end = 18.dp)
                    .rotate(angle)
                    .clickable {
                        isExpanded = !isExpanded
                    }
                //                    .scale(1f, if (isExpanded) -1f else 1f)
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column {
                content()
            }
        }
    }
}
