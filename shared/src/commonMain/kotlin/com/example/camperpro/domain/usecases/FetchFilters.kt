package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.composition.Filter
import com.example.camperpro.domain.repositories.FilterRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchFilters(private val filters: FilterRepository) : IBaseUsecase {
    suspend operator fun invoke(category: String): ResultWrapper<List<Filter>> {
        return ResultWrapper.Success(filters.allOfCategory(category))
    }
}

class RFetchFilters : KoinComponent {
    private val fetchFilters: FetchFilters by inject()
    suspend fun execute(category: String): List<Filter>? =
        fetchFilters.invoke(category).flattenIos()
}