package com.horionDev.climbingapp.utils

expect class LanguageManager(context: Any?) {
    fun getDeviceLanguage(): String
    fun getDeviceCountry(): String
}
