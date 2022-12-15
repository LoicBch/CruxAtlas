package com.example.camperpro.domain.repositories

import com.example.camperpro.domain.model.Ad
import com.jetbrains.kmm.shared.data.ResultWrapper

interface AdRepository {
    suspend fun all(): ResultWrapper<List<Ad>>
}