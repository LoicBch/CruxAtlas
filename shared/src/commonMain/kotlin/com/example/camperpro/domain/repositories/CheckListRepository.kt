package com.example.camperpro.domain.repositories

import com.example.camperpro.domain.model.CheckList
import com.jetbrains.kmm.shared.data.ResultWrapper

interface CheckListRepository {
    suspend fun all(): ResultWrapper<List<CheckList>>
}