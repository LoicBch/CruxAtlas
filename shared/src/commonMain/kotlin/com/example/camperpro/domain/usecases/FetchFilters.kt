package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.composition.Filter
import com.example.camperpro.domain.repositories.FilterRepository

class FetchFilters(private val filters: FilterRepository) : IBaseUsecase {
    suspend operator fun invoke(category: String): ResultWrapper<List<Filter>> {
        return ResultWrapper.Success(filters.allOfCategory(category))
    }
}