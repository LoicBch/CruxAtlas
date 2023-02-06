package com.example.camperpro.domain.usecases

import com.example.camperpro.data.repositories.Searches
import com.example.camperpro.domain.model.Search

class DeleteSearch(private val searches: Searches ): IBaseUsecase {
    suspend operator fun invoke(search: Search) {
         searches.delete(search)
    }
}