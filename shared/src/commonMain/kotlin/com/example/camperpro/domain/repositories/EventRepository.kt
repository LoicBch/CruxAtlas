package com.example.camperpro.domain.repositories

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.Event

interface EventRepository {
    suspend fun all(countriesFilters: String): ResultWrapper<List<Event>>
}