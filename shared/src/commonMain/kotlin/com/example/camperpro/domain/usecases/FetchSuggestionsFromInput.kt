package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.Place
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchSuggestionsFromInput(private val camperProApi: Api) : IBaseUsecase {
    suspend operator fun invoke(input: String): ResultWrapper<List<Place>> {
        return camperProApi.getLocationSuggestions(input)
    }
}

class RFetchSuggestionsFromInput : KoinComponent {
    private val fetchSuggestionsFromInput: FetchSuggestionsFromInput by inject()
    suspend fun execute(input: String): List<Place>? =
        fetchSuggestionsFromInput.invoke(input).flattenIos()
}