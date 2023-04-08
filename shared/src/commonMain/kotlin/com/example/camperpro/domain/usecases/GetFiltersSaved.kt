package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.composition.Filter
import com.example.camperpro.domain.repositories.FilterRepository

class GetFiltersSaved(val filters: FilterRepository) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<Filter>> {
        val filtersSaved = filters.getFilterSaved()
        return if (filtersSaved != null) {
            ResultWrapper.Success(filters.getFilterSaved())
        } else {
            ResultWrapper.Failure(0, Throwable("Null"))
        }
    }
}