package com.horionDev.climbingapp.android.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.domain.model.entities.Boulder
import com.horionDev.climbingapp.domain.model.entities.BoulderLog
import com.horionDev.climbingapp.domain.model.entities.Route
import com.horionDev.climbingapp.domain.model.entities.RouteLog

@Composable
fun BouldersTab(boulders: List<Pair<BoulderLog, Boulder>>) {
    Column {
//        BoulderStatistics()
        BoulderLogBook(boulders)
    }
}

@Composable
fun BoulderStatistics() {
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
fun BoulderLogBook(bouldersLog: List<Pair<BoulderLog, Boulder>>) {
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
    if (bouldersLog.isEmpty()) {
        Column {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "No boulders logged yet",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.oppinsedium))
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    } else {
        bouldersLog.forEach {
            BoulderLogItem(boulderLog = it)
        }
    }
}

@Composable
fun BoulderLogItem(boulderLog: Pair<BoulderLog, Boulder>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(5.dp))
            .background(color = Color.Black, shape = RoundedCornerShape(5.dp))
            .padding(8.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(text = boulderLog.first.boulderName, color = Color.Black)
                    Text(text = boulderLog.second.cragName, color = Color.Black)
                }
//                Text(text = routeLog.second.style, color = Color.Black)
                Text(text = "sports", color = Color.Black)
                Text(text = boulderLog.second.grade.grade, color = Color.Black)
            }
            Text(text = boulderLog.first.rating, color = Color.Black)
            Text(text = boulderLog.first.text, color = Color.Black)
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(text = boulderLog.first.rating, color = Color.Black)
            }
        }
    }
}