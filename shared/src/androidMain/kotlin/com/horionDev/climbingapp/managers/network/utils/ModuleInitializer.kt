package com.horionDev.climbingapp.managers.network.utils

import android.content.Context
import androidx.startup.Initializer

//A combiner avec les initializer des autres manager pour get le context
@Suppress("UNUSED")
internal class ModuleInitializer : Initializer<Int> {
    override fun create(context: Context): Int {
//        NetworkManager.configure(context)
        return 0
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}