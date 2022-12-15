package com.example.camperpro.android.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColor {
    val SelectedItem = Color(0xFF165DD7)
    val BlueCamperPro = Color(0xFF165DD7)
    val unSelectedFilter = Color(0xFF8A8FAB)
    val unFocusedTextField = Color(0xFFF4F4F4)
    val unSelectedFilterOption = Color(0xFFE0E0E0)
    val selectedFilterOption = unSelectedFilter
    val Black = Color(0xFF000000)
}

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)


val Yellow = Color(0xFFF7e6b7)
val YellowGradient = Brush.linearGradient(
    colors = listOf(Yellow, Color.Transparent),
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)