package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.model.News
import com.example.camperpro.domain.repositories.NewsRepository
import com.jetbrains.kmm.shared.data.ResultWrapper
import io.ktor.client.*

class AllNews(private var camperProApi: Api) : NewsRepository {
    override suspend fun all(): ResultWrapper<List<News>> {
        TODO("Not yet implemented")
    }
}