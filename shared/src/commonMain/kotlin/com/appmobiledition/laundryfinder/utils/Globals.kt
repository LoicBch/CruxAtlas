package com.appmobiledition.laundryfinder.utils

import com.appmobiledition.laundryfinder.domain.model.MenuLink
import com.appmobiledition.laundryfinder.domain.model.composition.Location
import kotlinx.coroutines.Job
import kotlin.native.concurrent.ThreadLocal

// TODO: replace by class with dependecy injection if this get to messy, (spoiler : it will)

object Globals {
    object geoLoc {
        var lastKnownLocation: Location = Constants.DEFAULT_LOCATION
        var lastSearchedLocation: Location = Constants.DEFAULT_LOCATION
        var RADIUS_AROUND_LIMIT = 100
        var locationObserver: Job? = null
        lateinit var deviceCountry: String
        lateinit var deviceLanguage: String
        lateinit var appLanguage: String
    }

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


    object network {
        var status: ConnectivityObserver.NetworkStatus =
            ConnectivityObserver.NetworkStatus.Unavailable
    }

    lateinit var menuLinks: List<MenuLink>
}

enum class FilterType {
    BRAND, SERVICE, COUNTRIES, UNSELECTED_DEALER, UNSELECTED_EVENT
}

enum class BottomSheetOption {
    FILTER, FILTER_EVENT, SORT, SORT_AROUND_PLACE, SORT_EVENTS, MAPLAYER
}