package com.example.camperpro.domain.usecases

import com.example.camperpro.data.repositories.Searches
import com.jetbrains.kmm.shared.data.ResultWrapper

class GetAllSearchForACategory(private val searches: Searches) : IBaseUsecase {
    suspend operator fun invoke(searchCategoryKey: String): ResultWrapper<List<String>> {
        return ResultWrapper.Success(
            searches.allOfCategory(searchCategoryKey)!!
                .sortedByDescending { it.timeStamp }
                .take(3)
                .map { it.searchLabel })
    }
}