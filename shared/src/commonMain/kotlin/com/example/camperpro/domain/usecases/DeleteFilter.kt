package com.example.camperpro.domain.usecases

import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.composition.Filter
import com.example.camperpro.domain.repositories.FilterRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteFilter(val filters: FilterRepository) : IBaseUsecase {
    suspend operator fun invoke(filter: Filter) {
        filters.delete(filter)
    }
}

class RDeleteFilter : KoinComponent {
    private val deleteFilter: DeleteFilter by inject()
    suspend fun execute(filter: Filter) = deleteFilter.invoke(filter)
}