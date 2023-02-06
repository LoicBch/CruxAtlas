package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.Dealer
import com.example.camperpro.domain.model.composition.distanceFromLastSearch
import com.example.camperpro.domain.model.composition.distanceFromUserLocation
import com.example.camperpro.utils.SortOption
import com.example.camperpro.data.ResultWrapper

class SortSpots() : IBaseUsecase {
    suspend operator fun invoke(
        sortOption: SortOption,
        spotsToSort: List<Dealer>
    ): ResultWrapper<List<Dealer>> {
        return when (sortOption) {
            SortOption.NONE -> {
                ResultWrapper.Success(spotsToSort)
            }
            SortOption.DIST_FROM_YOU -> {
                val sortedList = spotsToSort.toMutableList().apply {
                    sortWith(compareBy {
                        Location(it.latitude, it.longitude).distanceFromUserLocation
                    })
                }.toList()
                ResultWrapper.Success(sortedList)
            }
            SortOption.DIST_FROM_SEARCHED -> {
                val sortedList = spotsToSort.toMutableList().apply {
                    sortWith(compareBy {
                        Location(it.latitude, it.longitude).distanceFromLastSearch
                    })
                }.toList()
                ResultWrapper.Success(sortedList)
            }
            // TODO: will be event so make spot a supertype with attribute type that can be either event / dealer / partners
            SortOption.BY_DATE -> {
                ResultWrapper.Success(spotsToSort)
            }
        }
    }
}