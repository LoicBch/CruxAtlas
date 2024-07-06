package com.horionDev.climbingapp.managers.location.utils

import android.content.Context
import androidx.startup.Initializer
import com.horionDev.climbingapp.managers.location.LocationManager
import com.horionDev.climbingapp.managers.location.extension.configure


@Suppress("UNUSED")
internal class ModuleInitializer : Initializer<Int> {
    override fun create(context: Context): Int {
        LocationManager.configure(context)
        return 0
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}