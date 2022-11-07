package com.jetbrains.kmm.shared.data.datasources.remote

object HttpRoutes {

    const val GUEST = "https://guest.park4night.com/services/V4.1"
    const val SPOTS_FILTER = "/lieuxGetFilter.php?"

    fun constructUrl(latitude: String, longitude: String): String {
        return "$GUEST$SPOTS_FILTER&latitude=$latitude&longitude=$longitude&context_user=guest&context_os=ANDROID&context_lang=en&langue_locale=fr_FR&context_latitude=$latitude&context_longitude=$longitude&context_version=7.0.33&isMonthPremium=false&isYearPremium=false&context_id_user=guest&os=ANDROID&apikey="
    }

}