package com.example.camperpro.managers.location.utils

import android.content.Context
import androidx.startup.Initializer
import com.example.camperpro.managers.location.LocationManager
import com.example.camperpro.managers.location.extension.configure
import com.example.camperpro.managers.network.extension.configure


@Suppress("UNUSED")
internal class ModuleInitializer : Initializer<Int> {
    override fun create(context: Context): Int {
        LocationManager.configure(context)
        return 0
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}