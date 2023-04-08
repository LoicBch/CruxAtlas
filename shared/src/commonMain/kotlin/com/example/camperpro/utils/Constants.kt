package com.example.camperpro.utils

import com.example.camperpro.domain.model.composition.Location

object Constants {

    object API {
        const val DEALERS = "dealers.php"
        const val STARTER = "app_general.php"
        const val EVENTS = "events.php"
        const val LOCATE = "locate.php"
        const val CHECKLISTS = "checklists.php"
        const val ADS = "ads.php"
        const val GEOCODING = "geocoding.php"
    }

    object PreferencesKey {
        const val LAST_SETUP_TIMESTAMP = "LAST_SETUP_TIMESTAMP"
        const val METRIC = "METRIC"
    }

    //enum
    object Persistence {
        const val SEARCH_CATEGORY_LOCATION = "LOCATION"
    }

    val DEFAULT_LOCATION = Location(45.87, 2.50)
    const val METER = 0
    const val MILES = 1
    const val PLACE_QUERY_LIMIT = 100
    const val GPS_UPDATE_INTERVAL = 10000L
    const val SUGGESTION_START_LENGTH = 3
    const val WEBSERVICE_VERSION = "v4"
    const val BASE_URL = "https://camper-pro.com/services/$WEBSERVICE_VERSION/"
    const val A_PROPOS_URL = "https://www.camper-pro.com/apropos"
    const val HELP_URL = "https://camper-pro.com/404"
    const val PRIVACY_POLICY_URL = "https://www.camper-pro.com/cgv"
}

enum class SortOption {
    NONE, DIST_FROM_YOU, DIST_FROM_SEARCHED, BY_DATE
}