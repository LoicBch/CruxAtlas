package com.example.camperpro.utils

import com.example.camperpro.domain.model.composition.Location

object Constants {

    object API {
        const val DEALERS = "dealers.php"
        const val STARTER = "app_general.php"
        const val EVENTS = "events.php"
        const val ADS = "ads.php"
        const val GEOCODING = "geocoding.php"
    }

    object PreferencesKey {
        const val LAST_SETUP_TIMESTAMP = "LAST_SETUP_TIMESTAMP"
    }

    //enum
    object Persistence {
        const val SEARCH_CATEGORY_FILTER_SERVICE = "SEARCH_CATEGORY_FILTER_SERVICE"
        const val SEARCH_CATEGORY_FILTER_BRANDS = "SEARCH_CATEGORY_FILTER_BRANDS"
        const val SEARCH_CATEGORY_FILTER_ACCESSORIES = "SEARCH_CATEGORY_FILTER_ACCESSORIES"
        const val SEARCH_CATEGORY_LOCATION = "LOCATION"
    }

    val DEFAULT_LOCATION = Location(45.87, 2.50)
    const val RADIUS_AROUND_LIMIT = 1330
    const val GPS_UPDATE_INTERVAL = 10000L
    const val SUGGESTION_START_LENGTH = 3
    const val WEBSERVICE_VERSION = "v4"
    const val BASE_URL = "https://camper-pro.com/services/$WEBSERVICE_VERSION/"
}

enum class SortOption {
    NONE, DIST_FROM_YOU, DIST_FROM_SEARCHED, BY_DATE
}