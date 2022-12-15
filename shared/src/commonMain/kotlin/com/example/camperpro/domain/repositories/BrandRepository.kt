package com.example.camperpro.domain.repositories

import com.example.camperpro.domain.model.CheckList
import com.jetbrains.kmm.shared.data.ResultWrapper

interface BrandRepository {
    suspend fun all(): ResultWrapper<List<String>>
}