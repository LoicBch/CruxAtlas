package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.Search
import com.example.camperpro.domain.repositories.SearchesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAllSearchForACategory(private val searches: SearchesRepository) : IBaseUsecase {
    suspend operator fun invoke(searchCategoryKey: String): ResultWrapper<List<Search>> {
        return ResultWrapper.Success(
            searches.allOfCategory(searchCategoryKey)!!
                .sortedByDescending { it.timeStamp })
    }
}

class RGetAllSearchForACategory : KoinComponent {
    private val getAllSearchForACategory: GetAllSearchForACategory by inject()
    suspend fun execute(searchCategoryKey: String): List<Search>? =
        getAllSearchForACategory.invoke(searchCategoryKey).flattenIos()
}