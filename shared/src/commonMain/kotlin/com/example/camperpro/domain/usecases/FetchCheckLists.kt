package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.model.CheckList
import com.example.camperpro.domain.repositories.CheckListRepository
import com.jetbrains.kmm.shared.data.ResultWrapper

class FetchCheckLists(private val checkLists: CheckListRepository): IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<CheckList>> {
        return checkLists.all()
    }
}