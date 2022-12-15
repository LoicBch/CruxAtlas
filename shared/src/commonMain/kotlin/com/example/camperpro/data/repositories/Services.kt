package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.repositories.ServiceRepository
import com.jetbrains.kmm.shared.data.ResultWrapper
import io.ktor.client.*

class Services(private var camperProApi: Api) : ServiceRepository {
    override suspend fun all(): ResultWrapper<List<String>> {
        TODO("Not yet implemented")
    }
}