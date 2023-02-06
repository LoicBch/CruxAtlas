package com.example.camperpro.utils

import com.example.camperpro.domain.model.MenuLink
import com.example.camperpro.domain.model.composition.Location
import kotlinx.coroutines.Job
import kotlin.native.concurrent.ThreadLocal

// TODO: to replace by class with dependecy injection if this get to messy

object Globals {

    @ThreadLocal
    object geoLoc {
        var lastKnownLocation: Location? = null
        var lastSearchedLocation: Location? = null
        var locationObserver: Job? = null
        lateinit var deviceCountry: String
        lateinit var deviceLanguage: String
        lateinit var appLanguage: String
    }

    @ThreadLocal
    object filters {
        lateinit var brands: List<Pair<String, String>>
        lateinit var services: List<Pair<String, String>>
        var countries: List<String> =
            listOf(
                "Germany",
                "France",
                "Italy",
                "Belgium",
                "Denmark",
                "Finland",
                "Austria",
                "Portugal",
                "Spain",
                "United Kingdom"
            )
    }

    @ThreadLocal
    object network {
        var status: ConnectivityObserver.NetworkStatus =
            ConnectivityObserver.NetworkStatus.Unavailable
    }

    lateinit var menuLinks: List<MenuLink>
}


enum class BottomSheetOption {
    FILTER, FILTER_EVENT, SORT, SORT_AROUND_PLACE, SORT_EVENTS, MAPLAYER
}