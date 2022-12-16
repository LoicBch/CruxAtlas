package com.example.camperpro.utils

import android.content.Context
import android.os.Build

actual class LanguageManager actual constructor(val context: Any?) {

    actual fun getDeviceLanguage(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            (context as Context).resources.configuration.locales[0].language
        } else {
            (context as Context).resources.configuration.locale.language
        }
    }

    actual fun getDeviceCountry(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            (context as Context).resources.configuration.locales[0].country
        } else {
            (context as Context).resources.configuration.locale.country
        }
    }
}
