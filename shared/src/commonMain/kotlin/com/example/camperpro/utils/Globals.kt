package com.example.camperpro.utils

import com.example.camperpro.domain.model.Location
import com.example.camperpro.domain.model.MenuLink
import kotlinx.coroutines.Job

// to replace by class with dependecy injection if this get to messy
object Globals {
    object geoLoc {
        var lastKnownLocation: Location? = null
        var lastSearchedLocation: Location? = null
        var locationObserver: Job? = null
        lateinit var deviceCountry: String
        lateinit var deviceLanguage: String
        lateinit var appLanguage: String
    }

    object filters {
        lateinit var brands: List<Pair<String, String>>
        lateinit var services: List<Pair<String, String>>
    }

    object internet {
        var isConnected: Boolean = false
    }

    var currentBottomSheetOption: BottomSheetOption = BottomSheetOption.FILTER
    var lastTimeStarterWasLoaded: Int = 0
    lateinit var menuLinks: List<MenuLink>
}


enum class BottomSheetOption{
    FILTER, SORT, MAPLAYER
}