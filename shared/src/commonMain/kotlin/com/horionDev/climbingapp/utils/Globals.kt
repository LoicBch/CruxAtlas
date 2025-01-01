package com.horionDev.climbingapp.utils

import com.horionDev.climbingapp.domain.model.composition.Location
import kotlinx.coroutines.Job

// TODO: replace by class with dependecy injection if this get to messy, (spoiler : it will)

object Globals {
    object GeoLoc {
        var lastKnownLocation: Location = Constants.DEFAULT_LOCATION
        var lastSearchedLocation: Location = Constants.DEFAULT_LOCATION
        var RADIUS_AROUND_LIMIT = 100
        var locationObserver: Job? = null
        lateinit var deviceCountry: String
        lateinit var deviceLanguage: String
        lateinit var appLanguage: String
    }
    object network {
        var status: ConnectivityObserver.NetworkStatus =
            ConnectivityObserver.NetworkStatus.Unavailable
    }
}

enum class BottomSheetOption {
    FILTER, FILTER_EVENT, SORT, SORT_AROUND_PLACE, SORT_EVENTS, MAPLAYER
}