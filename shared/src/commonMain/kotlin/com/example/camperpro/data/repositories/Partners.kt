package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.repositories.PartnerRepository

class Partners(private var camperProApi: Api): PartnerRepository {
    override suspend fun all() = camperProApi.getPartners()
}