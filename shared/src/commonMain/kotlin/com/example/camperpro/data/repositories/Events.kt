package com.example.camperpro.data.repositories

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.model.Event
import com.example.camperpro.domain.repositories.EventRepository

class Events(private var camperProApi: Api) : EventRepository {
    override suspend fun all(countriesFilters: String): ResultWrapper<List<Event>> {
        return camperProApi.getEvents(countriesFilters)
    }
}
