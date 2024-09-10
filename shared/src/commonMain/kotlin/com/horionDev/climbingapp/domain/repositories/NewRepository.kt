package com.horionDev.climbingapp.domain.repositories

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.model.NewsItem
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse

interface NewRepository {
    suspend fun getNews(page: Int): ResultWrapper<List<NewsItem>, ErrorResponse>
}