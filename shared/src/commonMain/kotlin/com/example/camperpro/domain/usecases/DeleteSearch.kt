package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.model.Search
import com.example.camperpro.domain.repositories.SearchesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteSearch(private val searches: SearchesRepository) : IBaseUsecase {
    suspend operator fun invoke(search: Search) {
        searches.delete(search)
    }
}

class RDeleteSearch : KoinComponent {
    private val deleteSearch: DeleteSearch by inject()
    suspend fun execute(search: Search) =
        deleteSearch.invoke(search)
}