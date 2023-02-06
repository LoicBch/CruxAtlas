package com.example.camperpro.domain.repositories

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.Dealer

interface DealerRepository {
    suspend fun atLocation(location: Location): ResultWrapper<List<Dealer>>
}