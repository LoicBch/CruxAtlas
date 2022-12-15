package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.repositories.BrandRepository
import com.jetbrains.kmm.shared.data.ResultWrapper
import io.ktor.client.*

class Brands(private var camperProApi: Api) : BrandRepository {
    override suspend fun all(): ResultWrapper<List<String>> {
        TODO("Not yet implemented")
    }
}