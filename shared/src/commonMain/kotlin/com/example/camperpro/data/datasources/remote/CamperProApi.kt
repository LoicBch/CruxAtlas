package com.example.camperpro.data.datasources.remote

import com.example.camperpro.data.model.responses.AdResponse
import com.example.camperpro.data.model.responses.SpotResponse
import com.example.camperpro.data.model.responses.StarterResponse
import com.example.camperpro.domain.model.*
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.Globals
import com.jetbrains.kmm.shared.data.ResultWrapper
import com.jetbrains.kmm.shared.data.safeApiCall
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import toVo

fun URLBuilder.addAppContext() {
    parameters.append("ctx_country", Globals.Location.deviceCountry)
    parameters.append("ctx_language", Globals.Location.deviceLanguage)
    parameters.append("ctx_app_language", Globals.Location.appLanguage)
}

// TODO: One Api per model 
class CamperProApi(private var client: HttpClient) : Api {

    override suspend fun starter(): ResultWrapper<Starter> {
        return safeApiCall {
            client.get {
                url(Constants.API.starter) {
                    addAppContext()
                }
            }.body<StarterResponse>().toVo()
        }
    }

    override suspend fun getSpotAtLocation(location: Location): ResultWrapper<List<Spot>> {
        return safeApiCall {
            client.get {
                url(Constants.API.dealers) {
                    parameters.append("lat", location.latitude.toString())
                    parameters.append("lon", location.longitude.toString())
                }
            }.body<SpotResponse>().spots.toVo()
        }
    }

    override suspend fun getAds(): ResultWrapper<List<Ad>> {
        return safeApiCall {
            client.get {
                url(Constants.API.ad)
            }.body<AdResponse>().ads.toVo()
        }
    }

    override suspend fun getPartners(): ResultWrapper<List<Partner>> {
        TODO()
    }
}
