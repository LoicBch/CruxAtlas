package com.example.camperpro.domain.repositories

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.Partner

interface PartnerRepository {
    suspend fun all(): ResultWrapper<List<Partner>>
}