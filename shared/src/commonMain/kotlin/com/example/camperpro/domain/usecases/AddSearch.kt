package com.example.camperpro.domain.usecases

import com.example.camperpro.data.repositories.Searches
import com.example.camperpro.domain.model.Search
import com.example.camperpro.domain.repositories.SearchesRepository

class AddSearch(private val searches: SearchesRepository): IBaseUsecase {
    suspend operator fun invoke(search: Search) {
        searches.add(search)
    }
}

