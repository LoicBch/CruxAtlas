package com.example.camperpro.domain.repositories

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.Dealer
import com.example.camperpro.domain.model.composition.Location

interface DealerRepository {
    suspend fun atLocation(
        location: Location,
        brandFilters: List<Int>?,
        serviceFilters: List<Int>?
    ): ResultWrapper<List<Dealer>>
}