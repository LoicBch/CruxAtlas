package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.model.Location
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.domain.model.distanceFromLastSearch
import com.example.camperpro.domain.model.distanceFromUserLocation
import com.example.camperpro.utils.SortOption
import com.jetbrains.kmm.shared.data.ResultWrapper

class SortSpots() : IBaseUsecase {
    suspend operator fun invoke(
        sortOption: SortOption,
        spotsToSort: List<Spot>
    ): ResultWrapper<List<Spot>> {
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