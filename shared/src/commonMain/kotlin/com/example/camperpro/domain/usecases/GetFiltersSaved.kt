package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.composition.Filter
import com.example.camperpro.domain.repositories.FilterRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

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

class RGetFiltersSaved : KoinComponent {
    private val getFiltersSaved: GetFiltersSaved by inject()
    suspend fun execute(): List<Filter>? =
        getFiltersSaved.invoke().flattenIos()
}