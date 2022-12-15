package com.example.camperpro.domain.repositories

import com.jetbrains.kmm.shared.data.ResultWrapper

interface ServiceRepository {
    suspend fun all(): ResultWrapper<List<String>>
}