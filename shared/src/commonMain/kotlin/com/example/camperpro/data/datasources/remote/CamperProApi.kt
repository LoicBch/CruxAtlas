package com.example.camperpro.data.datasources.remote

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.model.dto.AdDto
import com.example.camperpro.data.model.dto.EventDto
import com.example.camperpro.data.model.responses.DealerResponse
import com.example.camperpro.data.model.responses.PartnerResponse
import com.example.camperpro.data.model.responses.StarterResponse
import com.example.camperpro.data.model.responses.SuggestionResponse
import com.example.camperpro.data.safeApiCall
import com.example.camperpro.domain.model.*
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.Globals
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import toPlaces
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
            }.body<StarterResponse>().toVo()
        }
    }

    override suspend fun getSpotAtLocation(location: Location): ResultWrapper<List<Dealer>> {
        return safeApiCall {
            client.get(Constants.API.DEALERS) {
                url {
                    parameters.append("lat", location.latitude.toString())
                    parameters.append("lon", location.longitude.toString())
                }
            }.body<DealerResponse>().dealers.toVo()
        }
    }

    override suspend fun getAds(): ResultWrapper<List<Ad>> {
        return safeApiCall {
            client.get {
                url(Constants.API.ADS)
            }.body<List<AdDto>>().toVo()
        }
    }

    override suspend fun getLocationSuggestions(input: String): ResultWrapper<List<Place>> {
        return safeApiCall {
            client.get(Constants.API.GEOCODING) {
                url {
                    parameters.append("q", input)
                }
            }.body<SuggestionResponse>().toPlaces()
        }
    }

    override suspend fun getEvents(): ResultWrapper<List<Event>> {
        return safeApiCall {
            client.get(Constants.API.EVENTS).body<List<EventDto>>().toVo()
        }
    }

    override suspend fun getPartners(): ResultWrapper<List<Partner>> {
        return safeApiCall {
            client.get(Constants.API.DEALERS) {
                url {
                    parameters.append("type", "partner")
                }
            }.body<PartnerResponse>().partners.toVo()
        }
    }

}
