package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.Event
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.distanceFromUserLocation
import com.example.camperpro.utils.SortOption
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SortEvents : IBaseUsecase {
    suspend operator fun invoke(
        sortOption: SortOption,
        eventsToSort: List<Event>
    ): ResultWrapper<List<Event>> {
        return when (sortOption) {
            SortOption.NONE -> {
                ResultWrapper.Success(eventsToSort)
            }
            SortOption.DIST_FROM_YOU -> {
                val sortedList = eventsToSort.toMutableList().apply {
                    sortWith(compareBy {
                        Location(it.latitude, it.longitude).distanceFromUserLocation
                    })
                }.toList()
                ResultWrapper.Success(sortedList)
            }
            SortOption.BY_DATE -> {
                val sortedList = eventsToSort.toMutableList().apply {
                    sortWith(compareBy {
                        it.dateBegin
                    })
                }.toList()
                ResultWrapper.Success(sortedList)
            }
            else -> {
                ResultWrapper.Success(eventsToSort)
            }
        }
    }
}

class RSortEvents : KoinComponent {
    private val sortEvents: SortEvents by inject()
    suspend fun execute(sortOption: SortOption, eventsToSort: List<Event>): List<Event>? =
        sortEvents.invoke(sortOption, eventsToSort).flattenIos()
}