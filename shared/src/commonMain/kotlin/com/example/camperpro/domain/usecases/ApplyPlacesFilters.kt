package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.model.composition.Filter
import com.example.camperpro.domain.repositories.FilterRepository
import com.example.camperpro.utils.FilterType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApplyPlacesFilters(private val filters: FilterRepository) : IBaseUsecase {
    suspend operator fun invoke(filter: Filter) {
        when (filter.category) {
            FilterType.UNSELECTED_DEALER -> filters.resetActiveDealerFilter()
            FilterType.UNSELECTED_EVENT -> filters.resetActiveEventFilter()
            else -> {
                filters.add(filter)
            }
        }
    }
}

class RApplyPlacesFilters : KoinComponent {
    private val applyPlacesFilters: ApplyPlacesFilters by inject()
    suspend fun execute(filter: Filter) = applyPlacesFilters.invoke(filter)
}