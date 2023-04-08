package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.model.composition.Filter
import com.example.camperpro.domain.repositories.FilterRepository

class DeleteFilter(val filters: FilterRepository) : IBaseUsecase {
    suspend operator fun invoke(filter: Filter) {
        filters.delete(filter)
    }
}