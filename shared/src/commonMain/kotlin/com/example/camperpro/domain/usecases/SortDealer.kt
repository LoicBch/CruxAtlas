package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.Dealer
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.distanceFromLastSearch
import com.example.camperpro.domain.model.composition.distanceFromUserLocation
import com.example.camperpro.utils.SortOption

class SortDealer : IBaseUsecase {
    suspend operator fun invoke(
        sortOption: SortOption,
        dealersToSort: List<Dealer>
    ): ResultWrapper<List<Dealer>> {
        return when (sortOption) {
            SortOption.NONE -> {
                ResultWrapper.Success(dealersToSort)
            }
            SortOption.DIST_FROM_YOU -> {
                val sortedList = dealersToSort.toMutableList().apply {
                    sortWith(compareBy {
                        Location(it.latitude, it.longitude).distanceFromUserLocation
                    })
                }.toList()
                ResultWrapper.Success(sortedList)
            }
            SortOption.DIST_FROM_SEARCHED -> {
                val sortedList = dealersToSort.toMutableList().apply {
                    sortWith(compareBy {
                        Location(it.latitude, it.longitude).distanceFromLastSearch
                    })
                }.toList()
                ResultWrapper.Success(sortedList)
            }
            else -> {
                ResultWrapper.Success(dealersToSort)
            }
        }
    }
}