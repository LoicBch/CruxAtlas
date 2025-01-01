package com.horionDev.climbingapp.utils.di

import com.horionDev.climbingapp.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}


@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
class AndroidDependencyProvider {
    fun initDependencies(modelInitializer: ModelInitializer) {
        val androidModule = module {
            single<ModelInitializer> { modelInitializer }
        }
        startKoin {
            modules(sharedModule().toMutableList().apply { add(androidModule) }.toList())
        }.koin
    }
}