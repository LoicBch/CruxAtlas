package com.horionDev.climbingapp.utils

import com.horionDev.climbingapp.domain.model.composition.Location

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

    object Preferences {
        const val USER_ID = "user_id"
        const val SESSION_TOKEN = "session_token"
        const val intNotSavedInPreferences = -1
    }

    object PreferencesKey {
        const val LAST_SETUP_TIMESTAMP = "LAST_SETUP_TIMESTAMP"
        const val METRIC = "METRIC"
        const val GRADING_SYSTEM = "GRADING_SYSTEM"
    }

    //enum
    object Persistence {
        const val SEARCH_CATEGORY_LOCATION = "LOCATION"
    }

    val DEFAULT_LOCATION = Location(45.87, 2.50)
    const val METER = 0
    const val MILES = 1
    const val AMERICAN_YDS = 0
    const val FRENCH = 1
    const val UIAA = 2
    const val PLACE_QUERY_LIMIT = 100
    const val GPS_UPDATE_INTERVAL = 10000L
    const val SUGGESTION_START_LENGTH = 3
    const val WEBSERVICE_VERSION = "v4"
    const val BASE_URLL = "http://192.168.1.115:8080/"
//    const val BASE_URL = "http://0.0.0.0:8080/"
//    const val BASE_URL = "http://127.0.0.1:8080/"
//    const val BASE_URL = "http://185.170.58.30:8080/"
    const val A_PROPOS_URL = "https://www.camper-pro.com/apropos"
    const val HELP_URL = "https://camper-pro.com/404"
    const val PRIVACY_POLICY_URL = "https://www.camper-pro.com/cgv"
}

enum class SortOption {
    NONE, DIST_FROM_YOU, DIST_FROM_SEARCHED, BY_DATE
}

enum class Country(val displayValue: String) {
    FRANCE("\uD83C\uDDEB\uD83C\uDDF7 France"),
    SPAIN("\uD83C\uDDEA\uD83C\uDDF8 Spain"),
    ITALY("\uD83C\uDDEE\uD83C\uDDF9 Italy"),
    GREECE("\uD83C\uDDEC\uD83C\uDDF7 Greece"),
    GERMANY("\uD83C\uDDE9\uD83C\uDDEA Germany"),
    SWITZERLAND("\uD83C\uDDE8\uD83C\uDDED Switzerland"),
    AUSTRIA("\uD83C\uDDE6\uD83C\uDDF9 Austria"),
    BELGIUM("\uD83C\uDDE7\uD83C\uDDEA Belgium"),
    NETHERLANDS("\uD83C\uDDF3\uD83C\uDDF1 Netherlands"),
    LUXEMBOURG("\uD83C\uDDF1\uD83C\uDDFA Luxembourg"),
    PORTUGAL("\uD83C\uDDF5\uD83C\uDDF9 Portugal"),
    UK("\uD83C\uDDEC\uD83C\uDDE7 United Kingdom"),
    IRELAND("\uD83C\uDDEE\uD83C\uDDEA Ireland"),
    DENMARK("\uD83C\uDDE9\uD83C\uDDF0 Denmark"),
    SWEDEN("\uD83C\uDDF8\uD83C\uDDEA Sweden"),
    NORWAY("\uD83C\uDDF3\uD83C\uDDF4 Norway"),
    FINLAND("\uD83C\uDDEB\uD83C\uDDEE Finland"),
    ICELAND("\uD83C\uDDEE\uD83C\uDDF8 Iceland"),
    CZECH_REPUBLIC("\uD83C\uDDE8\uD83C\uDDFF Czech Republic"),
    SLOVAKIA("\uD83C\uDDF8\uD83C\uDDF0 Slovakia"),
    SLOVENIA("\uD83C\uDDF8\uD83C\uDDEE Slovenia"),
    CROATIA("\uD83C\uDDED\uD83C\uDDF7 Croatia"),
    BOSNIA("\uD83C\uDDE7\uD83C\uDDE6 Bosnia & Herzegovina"),
    SERBIA("\uD83C\uDDF7\uD83C\uDDF8 Serbia"),
    MONTENEGRO("\uD83C\uDDF2\uD83C\uDDEA Montenegro"),
    ALBANIA("\uD83C\uDDE6\uD83C\uDDF1 Albania"),
    MACEDONIA("\uD83C\uDDF2\uD83C\uDDF0 North Macedonia"),
    KOSOVO("\uD83C\uDDFD\uD83C\uDDF0 Kosovo"),
    BULGARIA("\uD83C\uDDE7\uD83C\uDDEC Bulgaria"),
    ROMANIA("\uD83C\uDDF7\uD83C\uDDF4 Romania"),
    HUNGARY("\uD83C\uDDED\uD83C\uDDFA Hungary"),
    POLAND("\uD83C\uDDF5\uD83C\uDDF1 Poland"),
    LITHUANIA("\uD83C\uDDF1\uD83C\uDDF9 Lithuania"),
    LATVIA("\uD83C\uDDF1\uD83C\uDDFB Latvia"),
    ESTONIA("\uD83C\uDDEA\uD83C\uDDEA Estonia"),
    RUSSIA("\uD83C\uDDF7\uD83C\uDDFA Russia"),
    UKRAINE("\uD83C\uDDFA\uD83C\uDDE6 Ukraine"),
    BELARUS("\uD83C\uDDE7\uD83C\uDDFE Belarus"),
    MOLDOVA("\uD83C\uDDF2\uD83C\uDDE9 Moldova"),
    TURKEY("\uD83C\uDDF9\uD83C\uDDF7 Turkey"),
    CYPRUS("\uD83C\uDDE8\uD83C\uDDFE Cyprus"),
    MALTA("\uD83C\uDDF2\uD83C\uDDF9 Malta"),
    ANDORRA("\uD83C\uDDE6\uD83C\uDDE9 Andorra"),
    MONACO("\uD83C\uDDF2\uD83C\uDDE8 Monaco"),
    SAN_MARINO("\uD83C\uDDF8\uD83C\uDDF2 San Marino"),
    VATICAN("\uD83C\uDDFB\uD83C\uDDEE Vatican City"),
    LIECHTENSTEIN("\uD83C\uDDF1\uD83C\uDDEE Liechtenstein")
}

enum class GENDER {
    MALE, FEMALE
}

val ageList = listOf(
    5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
    36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
    51, 52, 53, 54, 55, 56, 57, 58, 59, 60
)

val weightList = listOf(
    "35 kg", "36 kg", "37 kg", "38 kg", "39 kg", "40 kg", "41 kg", "42 kg", "43 kg", "44 kg",
    "45 kg", "46 kg", "47 kg", "48 kg", "49 kg", "50 kg", "51 kg", "52 kg", "53 kg", "54 kg",
    "55 kg", "56 kg", "57 kg", "58 kg", "59 kg", "60 kg", "61 kg", "62 kg", "63 kg", "64 kg",
    "65 kg", "66 kg", "67 kg", "68 kg", "69 kg", "70 kg", "71 kg", "72 kg", "73 kg", "74 kg",
    "75 kg", "76 kg", "77 kg", "78 kg", "79 kg", "80 kg", "81 kg", "82 kg", "83 kg", "84 kg",
    "85 kg", "86 kg", "87 kg", "88 kg", "89 kg", "90 kg"
)

val heightList = listOf(
    "145 cm", "146 cm", "147 cm", "148 cm", "149 cm", "150 cm", "151 cm", "152 cm", "153 cm", "154 cm",
    "155 cm", "156 cm", "157 cm", "158 cm", "159 cm", "160 cm", "161 cm", "162 cm", "163 cm", "164 cm",
    "165 cm", "166 cm", "167 cm", "168 cm", "169 cm", "170 cm", "171 cm", "172 cm", "173 cm", "174 cm",
    "175 cm", "176 cm", "177 cm", "178 cm", "179 cm", "180 cm", "181 cm", "182 cm", "183 cm", "184 cm",
    "185 cm", "186 cm", "187 cm", "188 cm", "189 cm", "190 cm"
)

val yearList = listOf(
    1960, 1961, 1962, 1963, 1964, 1965, 1966, 1967, 1968, 1969, 1970, 1971, 1972, 1973, 1974, 1975,
    1976, 1977, 1978, 1979, 1980, 1981, 1982, 1983, 1984, 1985, 1986, 1987, 1988, 1989, 1990, 1991,
    1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007,
    2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024
)