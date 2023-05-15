package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.model.Search
import com.example.camperpro.domain.repositories.SearchesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddSearch(private val searches: SearchesRepository) : IBaseUsecase {
    suspend operator fun invoke(search: Search) {
        searches.add(search)
    }
}

class RAddSearch : KoinComponent {
    private val addSearch: AddSearch by inject()
    suspend fun execute(search: Search) =
        addSearch.invoke(search)
}

