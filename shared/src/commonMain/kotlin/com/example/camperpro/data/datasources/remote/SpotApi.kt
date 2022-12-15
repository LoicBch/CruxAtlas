package com.example.camperpro.data.datasources.remote

import com.example.camperpro.data.model.responses.SpotResponse
import com.example.camperpro.domain.model.Spot
import com.jetbrains.kmm.shared.data.ResultWrapper
import com.jetbrains.kmm.shared.data.safeApiCall
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import toVo

class SpotApi(private var client: HttpClient) : Api {

    override suspend fun getAllSpots(): ResultWrapper<List<Spot>> {

        var vare = safeApiCall {
            client.get {
                url("https://camper-pro.com/services/v3/dealers.php")
            }
        }

        if (vare is ResultWrapper.Success) println(vare.value?.body())

        return safeApiCall {
            client.get {
                url("https://camper-pro.com/services/v3/dealers.php")
            }.body<SpotResponse>().spots.toVo()
        }


    }

    override suspend fun test(): SpotResponse {
       return client.get {
            url("https://camper-pro.com/services/v3/dealers.php")
        }.body()
    }

//    override suspend fun getSpotAtLocation(location: Location): ResultWrapper<SpotResponse> {
//        return safeApiCall {
//            client.get {
//                url("https://camper-pro.com/services/v3/dealers.php")
//            }.body<SpotResponse>()
//        }
//    }
}
