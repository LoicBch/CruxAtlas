package com.example.camperpro.domain.repositories

import com.example.camperpro.domain.model.News
import com.jetbrains.kmm.shared.data.ResultWrapper

interface NewsRepository {
    suspend fun all(): ResultWrapper<List<News>>
}