package com.appmobiledition.laundryfinder.android.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColor {

    val Primary = Color(0xFF556DD1)
    val Secondary = Color(0xFF6750A4)
    val Tertiary = Color(0xFF42556E)
    val PrimaryContainer = Color(0xFFDDE2F6)
    val SplashScreenBackground = Color(0xFF4457A7)
    val ClearGrey = Color(0x6649454F)
    val Red = Color(0xFFDC362E)
    val greyTags = Color(0xFFF4F6F9)

    val Primary30 = Color(0xFF33417D)
    val Primary40 = Color(0xFF4457A7)
    val Primary70 = Color(0xFF99A7E3)
    val OnPrimaryContainer = Color(0xFFDDE2F6)

    val Tertiary70 = Color(0xFF91A3BD)
    val Tertiary50 = Color(0xFF5F7AA0)
    val Tertiary40 = Color(0xFF516787)
    val Tertiary30 = Color(0xFF42556E)
    val Tertiary10 = Color(0xFF1C242F)
    val Variant = Color(0xFFCAC4D0)

    val neutral50 = Color(0xFF787579)
    val neutral20 = Color(0xFF313033)
    val lightgrey = Color(0xFFEEF2F6)
    val greenVerified = Color(0xFF51A46D)




    val SelectedItem = Color(0xFF165DD7)
    val ClearBlue = Color(0xFFeef0fa)
    val BlueCamperPro = Color(0xFF165DD7)
    val unSelectedFilter = Color(0xFF8A8FAB)
    val unFocusedTextField = Color(0xFFF4F4F4)
    val unSelectedFilterOption = Color(0xFFE0E0E0)
    val selectedFilterOption = unSelectedFilter
    val Black = Color(0xFF000000)
    val neutralText = Color(0xFF787579)
    val outlineText = Color(0xFFCAC4D0)
    val suggestionsLabel = Color(0xFF33417D)

    val BlueToWhite = Brush.linearGradient(
        colors = listOf(Primary, Color.White),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    val WhiteToBlue = Brush.linearGradient(
        colors = listOf(Color.White, Primary),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )
}

object M3 {

}


val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)


val Yellow = Color(0xFFF7e6b7)
