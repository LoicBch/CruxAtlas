package com.example.camperpro.utils

expect class LanguageManager(context: Any?) {
    fun getDeviceLanguage(): String
    fun getDeviceCountry(): String
}
