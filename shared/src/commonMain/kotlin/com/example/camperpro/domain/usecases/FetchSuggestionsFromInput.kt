package com.example.camperpro.domain.usecases

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.model.Location
import com.jetbrains.kmm.shared.data.ResultWrapper

class FetchSuggestionsFromInput(private val camperProApi: Api) : IBaseUsecase {
    suspend operator fun invoke(input: String): ResultWrapper<List<Pair<String, Location>>> {
        return camperProApi.getLocationSuggestions(input)
    }
}