package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.Dealer
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.distanceFromUserLocation
import com.example.camperpro.domain.repositories.DealerRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchDealersAtLocationUseCase(private val dealers: DealerRepository) : IBaseUsecase {

    suspend operator fun invoke(location: Location): ResultWrapper<List<Dealer>> {

        return when (val result = dealers.atLocation(location)) {
            is ResultWrapper.Success -> {
                ResultWrapper.Success(
                    result.value!!.toMutableList().apply {
                        sortWith(compareBy {
                            Location(it.latitude, it.longitude).distanceFromUserLocation
                        })
                    }.toList()
                )
            }
            is ResultWrapper.Failure -> {
                result
            }
        }
    }

    suspend fun executeIos(location: Location): List<Dealer> {
        val res = dealers.atLocation(location)
        return if (res is ResultWrapper.Success) {
            res.value!!.toMutableList().apply {
                sortWith(compareBy {
                    Location(it.latitude, it.longitude).distanceFromUserLocation
                })
            }.toList()
        } else {
            emptyList()
        }
    }
}

//Replica Swift DI
class RFetchSpotAtLocationUseCase : KoinComponent {
    private val fetchDealersAtLocationUseCase: FetchDealersAtLocationUseCase by inject()
    suspend fun execute(location: Location): List<Dealer> =
        fetchDealersAtLocationUseCase.executeIos(location)
}