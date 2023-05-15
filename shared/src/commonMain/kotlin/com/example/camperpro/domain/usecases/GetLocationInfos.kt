package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.LocationInfos
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetLocationInfos(val api: Api) : IBaseUsecase {
    suspend operator fun invoke(location: Location): ResultWrapper<LocationInfos> {
        return api.locate(location.latitude.toString(), location.longitude.toString())
    }
}

class RGetLocationInfos : KoinComponent {
    private val getLocationInfos: GetLocationInfos by inject()
    suspend fun execute(location: Location): LocationInfos? =
        getLocationInfos.invoke(location).flattenIos()
}