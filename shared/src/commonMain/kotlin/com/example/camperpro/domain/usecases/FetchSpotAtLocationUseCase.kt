package com.example.camperpro.domain.usecases

import com.jetbrains.kmm.shared.data.ResultWrapper
import com.example.camperpro.data.datasources.remote.SpotsService
import com.jetbrains.kmm.shared.domain.model.Location
import com.example.camperpro.domain.model.Spot
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchSpotAtLocationUseCase(private val spotService: SpotsService) : IBaseUsecase {

    suspend operator fun invoke(location: Location): ResultWrapper<List<Spot>> {


        return spotService.getSpotsAround(location)
    }

    suspend fun executeIos(location: Location): List<Spot> {
        val res = spotService.getSpotsAround(location)
        return if (res is ResultWrapper.Success) {
            res.value!!
        } else {
            emptyList()
        }
    }
}

//Replica Swift DI
class RFetchSpotAtLocationUseCase : KoinComponent {
    private val fetchSpotAtLocationUseCase: FetchSpotAtLocationUseCase by inject()
    suspend fun execute(location: Location): List<Spot> =
        fetchSpotAtLocationUseCase.executeIos(location)
}