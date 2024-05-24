package com.appmobiledition.laundryfinder.utils

expect class LanguageManager(context: Any?) {
    fun getDeviceLanguage(): String
    fun getDeviceCountry(): String
}
