package com.example.camperpro.domain.usecases

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.utils.Globals
import com.example.camperpro.utils.KMMPreference
import com.jetbrains.kmm.shared.data.ResultWrapper

class SetupApp(
    private val camperProApi: Api
) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<Nothing> {
        return when (val starter = camperProApi.starter()) {
            is ResultWrapper.Success -> {
                // TODO: Add persistence, does it really make sens tho ?
                starter.value?.let { const ->
                    Globals.filters.brands = const.filterBrands
                    Globals.filters.services = const.filterServices
                    Globals.menuLinks = const.menuLinks
                }
                ResultWrapper.Success(null)
            }
            is ResultWrapper.Failure -> starter
        }
    }
}

