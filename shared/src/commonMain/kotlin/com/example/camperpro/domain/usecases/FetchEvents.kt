package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.Event
import com.example.camperpro.domain.repositories.EventRepository
import com.example.camperpro.domain.repositories.FilterRepository
import com.example.camperpro.utils.FilterType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchEvents(
    private val events: EventRepository,
    private val filters: FilterRepository
) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<Event>> {

//        To add when WS will be done for event filter
//        val countryFilters =
//            filters.getFilterSaved()
//                ?.filter { it.category == FilterType.COUNTRIES && it.isSelected }
//                ?.map { it.filterId }
//
//        return events.all(countryFilters?.first())
        return events.all(null)
    }
}

class RFetchEvents : KoinComponent {
    private val fetchEvents: FetchEvents by inject()
    suspend fun execute(): List<Event>? =
        fetchEvents.invoke().flattenIos()
}