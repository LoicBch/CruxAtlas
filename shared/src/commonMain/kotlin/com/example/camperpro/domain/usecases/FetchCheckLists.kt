package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.CheckList
import com.example.camperpro.domain.repositories.CheckListRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchCheckLists(private val checkLists: CheckListRepository) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<CheckList>> {
        return checkLists.all()
    }
}

class RFetchCheckLists : KoinComponent {
    private val fetchCheckLists: FetchCheckLists by inject()
    suspend fun execute(): List<CheckList>? =
        fetchCheckLists.invoke().flattenIos()
}