package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.data.flattenIos
import com.example.camperpro.utils.Globals
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetupApp(
    private val camperProApi: Api
) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<Nothing> {
        return when (val starter = camperProApi.starter()) {
            is ResultWrapper.Success -> {
                starter.value?.let { const ->
                    Globals.filters.brands = const.filterBrands
                    Globals.filters.services = const.filterServices
                    Globals.menuLinks = const.menuLinks
                }
                Globals.filters.brands.forEach {
                    println(it)
                }
                ResultWrapper.Success(null)
            }
            is ResultWrapper.Failure -> starter
        }
    }
}

class RSetupApp : KoinComponent {
    private val setupApp: SetupApp by inject()
    suspend fun execute(): Any? =
        setupApp.invoke().flattenIos()
}

