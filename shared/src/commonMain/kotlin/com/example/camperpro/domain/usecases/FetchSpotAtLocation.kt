package com.example.camperproglobal.domain.usecases

import com.jetbrains.kmm.shared.data.ResultWrapper
import com.jetbrains.kmm.shared.data.datasources.remote.SpotsService
import com.jetbrains.kmm.shared.domain.model.Location
import com.jetbrains.kmm.shared.domain.model.Spot


//di
class FetchSpotAtLocation(var location: Location) : IFetchUseCase<List<Spot>> {
    private val spotService: SpotsService = SpotsService.create()


    override suspend fun execute(): ResultWrapper<List<Spot>> {
        return spotService.getSpotsAround(location)
    }

    suspend fun executeIos(): List<Spot> {
        val res = spotService.getSpotsAround(location)
        return if (res is ResultWrapper.Success) {
            res.value!!
        } else {
            emptyList()
        }
    }
}