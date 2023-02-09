package com.example.camperpro.android

import com.example.camperpro.android.destinations.DealerDetailsScreenDestination
import com.example.camperpro.android.destinations.EventDetailScreenDestination
import com.example.camperpro.android.destinations.PartnerDetailsScreenDestination

object AndroidConstants {
    val ScreensOverBottomBar = listOf(
        DealerDetailsScreenDestination,
        PartnerDetailsScreenDestination,
        EventDetailScreenDestination
    )
}