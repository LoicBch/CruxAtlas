package com.example.camperpro.domain.usecases

import com.example.camperpro.data.repositories.Dealers
import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.Dealer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchSpotAtLocationUseCase(private val dealers: Dealers) : IBaseUsecase {

    suspend operator fun invoke(location: Location): ResultWrapper<List<Dealer>> {
        return dealers.atLocation(location)
    }

    suspend fun executeIos(location: Location): List<Dealer> {
        val res = dealers.atLocation(location)
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
    suspend fun execute(location: Location): List<Dealer> =
        fetchSpotAtLocationUseCase.executeIos(location)
}