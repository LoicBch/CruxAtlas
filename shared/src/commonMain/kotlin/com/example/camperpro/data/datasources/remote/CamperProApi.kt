package com.example.camperpro.data.datasources.remote

import com.example.camperpro.data.model.responses.AdResponse
import com.example.camperpro.data.model.responses.SpotResponse
import com.example.camperpro.data.model.responses.StarterResponse
import com.example.camperpro.data.model.responses.SuggestionResponse
import com.example.camperpro.domain.model.*
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.Globals
import com.jetbrains.kmm.shared.data.ResultWrapper
import com.jetbrains.kmm.shared.data.safeApiCall
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import toPairList
import toVo

fun URLBuilder.addAppContext() {
    parameters.append("ctx_country", Globals.geoLoc.deviceCountry)
    parameters.append("ctx_language", Globals.geoLoc.deviceLanguage)
    parameters.append("ctx_app_language", Globals.geoLoc.appLanguage)
}

class CamperProApi(private var client: HttpClient) : Api {

    override suspend fun starter(): ResultWrapper<Starter> {
        return safeApiCall {
            client.get {
                url(Constants.API.STARTER)
//                {
//                    addAppContext()
//                }
            }.body<StarterResponse>().toVo()
        }
    }

    override suspend fun getSpotAtLocation(location: Location): ResultWrapper<List<Spot>> {
        return safeApiCall {
            client.get(Constants.API.DEALERS) {
                url {
                    parameters.append("lat", location.latitude.toString())
                    parameters.append("lon", location.longitude.toString())
                }
            }.body<SpotResponse>().spots.toVo()
        }
    }

    override suspend fun getAds(): ResultWrapper<List<Ad>> {
        return safeApiCall {
            client.get {
                url(Constants.API.ADS)
            }.body<AdResponse>().ads.toVo()
        }
    }

    override suspend fun getLocationSuggestions(input: String): ResultWrapper<List<Pair<String, Location>>> {
        return safeApiCall {
            client.get(Constants.API.ADS) {
                url{
                    parameters.append("q", input)
                }
            }.body<SuggestionResponse>().toPairList()
        }
    }

    override suspend fun getPartners(): ResultWrapper<List<Partner>> {
        TODO()
    }

}
