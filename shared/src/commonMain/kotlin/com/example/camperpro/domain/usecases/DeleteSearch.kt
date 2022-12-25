package com.example.camperpro.domain.usecases

import com.example.camperpro.data.repositories.Searches
import com.example.camperpro.domain.model.Search
import com.jetbrains.kmm.shared.data.ResultWrapper

class DeleteSearch(private val searches: Searches ): IBaseUsecase {
    suspend operator fun invoke(searchLabel: String) {
         searches.delete(searchLabel)
    }
}