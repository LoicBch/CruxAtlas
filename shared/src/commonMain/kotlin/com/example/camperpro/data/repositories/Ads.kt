package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.repositories.AdRepository
import com.jetbrains.kmm.shared.data.ResultWrapper

class Ads(private var camperProApi: Api) : AdRepository {
    override suspend fun all(): ResultWrapper<List<Ad>> {
        return camperProApi.getAds()
    }
}