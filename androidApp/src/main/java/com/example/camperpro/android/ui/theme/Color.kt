package com.example.camperpro.android.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColor {
    val Primary = Color(0xFF556DD1)
    val Secondary = Color(0xFF6750A4)
    val Tertiary = Color(0xFF42556E)
    val SelectedItem = Color(0xFF165DD7)
    val BlueCamperPro = Color(0xFF165DD7)
    val unSelectedFilter = Color(0xFF8A8FAB)
    val unFocusedTextField = Color(0xFFF4F4F4)
    val unSelectedFilterOption = Color(0xFFE0E0E0)
    val selectedFilterOption = unSelectedFilter
    val Black = Color(0xFF000000)
    val neutralText = Color(0xFF787579)
    val outlineText = Color(0xFFCAC4D0)
    val suggestionsLabel = Color(0xFF33417D)
}

object M3 {

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