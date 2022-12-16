package com.example.camperpro.utils

object Constants {

    object API {
        const val dealers = "dealers.php"
        const val starter = "app_general.php"
        const val partners = "partners.php"
        const val ad = "ads.php"
    }

    object PreferencesKey {
        const val LAST_SETUP_TIMESTAMP = "LAST_SETUP_TIMESTAMP"
    }

    const val LOCATION_UPDATE_RATE: Long = 300000
    const val WEBSERVICE_VERSION = "v4"
    const val BASE_URL = "https://camper-pro.com/services/$WEBSERVICE_VERSION/"
}