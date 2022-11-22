package com.example.camperpro.data.datasources.remote

import com.jetbrains.kmm.shared.data.ResultWrapper
import com.jetbrains.kmm.shared.data.model.responses.SpotResponse
import com.jetbrains.kmm.shared.data.safeApiCall
import com.example.camperpro.domain.model.Spot
import com.jetbrains.kmm.shared.domain.model.Location
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import toVo

class SpotsServiceImpl(private var client: HttpClient) : SpotsService {

    override suspend fun getSpotsAround(location: Location): ResultWrapper<List<Spot>> =
        safeApiCall {
            client.get {
                url(
                    HttpRoutes.constructUrl(
                        location.latitude.toString(),
                        location.longitude.toString()
                    )
                )
            }.body<SpotResponse>().spots.toVo()
        }
}