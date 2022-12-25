package com.example.camperpro.domain.usecases

import com.example.camperpro.data.repositories.Spots
import com.jetbrains.kmm.shared.data.ResultWrapper
import com.example.camperpro.domain.model.Location
import com.example.camperpro.domain.model.Spot
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchSpotAtLocationUseCase(private val spots: Spots) : IBaseUsecase {

    suspend operator fun invoke(location: Location): ResultWrapper<List<Spot>> {
        return spots.atLocation(location)
    }

    suspend fun executeIos(): List<Spot> {
        val res = spots.atLocation(Location(23.3, 23.3))
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
        fetchSpotAtLocationUseCase.executeIos()
}