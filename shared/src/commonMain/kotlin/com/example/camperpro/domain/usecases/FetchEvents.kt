package com.example.camperpro.domain.usecases

import com.example.camperpro.data.repositories.Events
import com.example.camperpro.domain.model.Event
import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.repositories.EventRepository

class FetchEvents(private val events: EventRepository): IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<Event>> {
        return events.all()
    }
}