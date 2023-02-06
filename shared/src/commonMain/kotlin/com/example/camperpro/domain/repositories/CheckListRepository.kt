package com.example.camperpro.domain.repositories

import com.example.camperpro.domain.model.CheckList
import com.example.camperpro.data.ResultWrapper

interface CheckListRepository {
    suspend fun all(): ResultWrapper<List<CheckList>>
}