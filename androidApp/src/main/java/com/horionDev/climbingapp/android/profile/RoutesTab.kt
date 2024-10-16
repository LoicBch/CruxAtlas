package com.horionDev.climbingapp.android.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.domain.model.entities.Route
import com.horionDev.climbingapp.domain.model.entities.RouteLog
import com.horionDev.climbingapp.domain.model.entities.RouteWithLog
import toVo

@Composable
fun RoutesTab(routes: List<RouteWithLog>) {
    Column {
//        Statistics()
        LogBook(routes)
    }
}

@Composable
fun Statistics() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Statistics",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.oppinsedium))
        )
    }

    Divider()
}

@Composable
fun LogBook(routes: List<RouteWithLog>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 21.dp)
    ) {
        Text(
            text = "LogBook",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.oppinsedium))
        )
    }

    Divider()

    if (routes.isEmpty()) {
        Column {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "No routes logged yet",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.oppinsedium))
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    } else {
        LazyColumn {
            items(routes) { routeLog ->
                RouteLogItem(routeLog = routeLog)
            }
        }
    }
}

@Composable
fun RouteLogItem(routeLog: RouteWithLog) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 12.dp, vertical = 8.dp)
//            .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(5.dp))
//            .background(color = Color.White, shape = RoundedCornerShape(5.dp))
//            .padding(8.dp)
//    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = routeLog.log.routeName, color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.oppinsedium))
                    )
                    Text(
                        text = routeLog.route.cragName, color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.oppinsedium))
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
//                Text(text = routeLog.second.style, color = Color.Black)
                Text(
                    modifier = Modifier.padding(end = 18.dp), text = "sports", color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )
                Row(
                    Modifier
                        .defaultMinSize(minWidth = 40.dp)
                        .padding(horizontal = 2.dp)
                        .background(
                            Color.Unspecified,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(vertical = 5.dp)
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = routeLog.route.toVo().grade, color = Color.Black)
                }
            }

            StarRatingBar(
                rating = routeLog.log.rating.toFloat()
            )

            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = routeLog.log.text,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.oppinsedium))
            )

            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(text = routeLog.log.date, color = Color.Black, fontFamily = FontFamily(Font(R.font.oppinsedium)))
            }
        }
    }
}

@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit = {}
) {
    val density = LocalDensity.current.density
    val starSize = (12f * density).dp
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = Icons.Filled.Star
            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color.Gray
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
//                    .selectable(
//                        selected = isSelected,
//                        onClick = {
//                            onRatingChanged(i.toFloat())
//                        }
//                    )
                    .width(starSize)
                    .height(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}