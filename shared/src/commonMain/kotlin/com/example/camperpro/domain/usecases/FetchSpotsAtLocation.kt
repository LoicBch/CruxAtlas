package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.Dealer
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.distanceFrom
import com.example.camperpro.domain.repositories.DealerRepository
import com.example.camperpro.domain.repositories.FilterRepository
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.FilterType
import com.example.camperpro.utils.Globals
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.roundToInt

class FetchDealersAtLocationUseCase(
    private val dealers: DealerRepository,
    private val filters: FilterRepository
) : IBaseUsecase {

    suspend operator fun invoke(
        location: Location
    ): ResultWrapper<List<Dealer>> {
        println(location.latitude.toString())

        val brandFilters =
            filters.getFilterSaved()
                ?.filter { it.category == FilterType.BRAND && it.isSelected }
                ?.map { it.filterId.toInt() }

        val serviceFilters =
            filters.getFilterSaved()
                ?.filter { it.category == FilterType.SERVICE && it.isSelected }
                ?.map { it.filterId.toInt() }

        return when (val result = dealers.atLocation(location, brandFilters, serviceFilters)) {
            is ResultWrapper.Success -> {

                val spotSorted = result.value!!.toMutableList().apply {
                    sortWith(compareBy {
                        Location(it.latitude, it.longitude).distanceFrom(location)
                    })
                }.toList()

                Globals.geoLoc.lastSearchedLocation = location

                ResultWrapper.Success(
                    if (result.value.size > Constants.PLACE_QUERY_LIMIT) {
                        spotSorted.subList(0, Constants.PLACE_QUERY_LIMIT).apply {
                            Globals.geoLoc.RADIUS_AROUND_LIMIT = Location(
                                this.last().latitude,
                                this.last().longitude
                            ).distanceFrom(location).roundToInt()
                        }
                    } else {
                        spotSorted.apply {
                            Globals.geoLoc.RADIUS_AROUND_LIMIT = Location(
                                this.last().latitude,
                                this.last().longitude
                            ).distanceFrom(location).roundToInt()
                        }
                    }
                )
            }
            is ResultWrapper.Failure -> {
                result
            }
        }
    }
}

// TODO: 'init()' has been explicitly marked unavailable here -> forced to make a replica for koin injection
class RFetchDealersAtLocationUseCase : KoinComponent {
    private val fetchDealersAtLocationUseCase: FetchDealersAtLocationUseCase by inject()
    suspend fun execute(location: Location): List<Dealer>? =
        fetchDealersAtLocationUseCase.invoke(location).flattenIos()
}