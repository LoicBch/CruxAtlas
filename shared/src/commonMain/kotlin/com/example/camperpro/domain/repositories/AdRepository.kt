package com.example.camperpro.domain.repositories

import com.example.camperpro.domain.model.Ad
import com.example.camperpro.data.ResultWrapper

interface AdRepository {
    suspend fun all(): ResultWrapper<List<Ad>>
}