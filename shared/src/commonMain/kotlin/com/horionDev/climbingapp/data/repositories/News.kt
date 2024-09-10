package com.horionDev.climbingapp.data.repositories

import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.domain.repositories.NewRepository

class News(private val cruxAtlasApi: Api) : NewRepository {
    override suspend fun getNews(page: Int) = cruxAtlasApi.getNews(page)
}