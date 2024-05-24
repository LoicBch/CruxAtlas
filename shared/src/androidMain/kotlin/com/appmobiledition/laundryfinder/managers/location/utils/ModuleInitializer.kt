package com.appmobiledition.laundryfinder.managers.location.utils

import android.content.Context
import androidx.startup.Initializer
import com.appmobiledition.laundryfinder.managers.location.LocationManager
import com.appmobiledition.laundryfinder.managers.location.extension.configure
import com.appmobiledition.laundryfinder.managers.network.extension.configure


@Suppress("UNUSED")
internal class ModuleInitializer : Initializer<Int> {
    override fun create(context: Context): Int {
        LocationManager.configure(context)
        return 0
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}