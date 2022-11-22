package com.example.camperpro.android.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val Yellow = Color(0xFFF7e6b7)
val SkyBlue = Color(0xFF88AFFF)
var SemiClearGrey = Color(0x80606060)
val YellowGradient = Brush.linearGradient(
    colors = listOf(Yellow, Color.Transparent),
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)