package com.example.camperpro.data.datasources.remote

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.data.model.dto.AdDto
import com.example.camperpro.data.model.dto.CheckListDto
import com.example.camperpro.data.model.dto.EventDto
import com.example.camperpro.data.model.responses.*
import com.example.camperpro.data.safeApiCall
import com.example.camperpro.domain.model.*
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.LocationInfos
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

    override suspend fun getSpotAtLocation(
        location: Location,
        brandFilters: List<Int>?,
        serviceFilters: List<Int>?
    ): ResultWrapper<List<Dealer>> {
        return safeApiCall {
            client.get(Constants.API.DEALERS) {
                url {
                    parameters.append("lat", location.latitude.toString())
                    parameters.append("lon", location.longitude.toString())

                    if (!brandFilters.isNullOrEmpty()) {
                        parameters.append("brand_filter", brandFilters.first().toString())
                    }
                    if (!serviceFilters.isNullOrEmpty()) {
                        parameters.append("service_filter", serviceFilters.first().toString())
                    }
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

    override suspend fun getChecklists(): ResultWrapper<List<CheckList>> {
        return safeApiCall {
            client.get(Constants.API.CHECKLISTS).body<List<CheckListDto>>().toVo()
        }
    }


    override suspend fun getEvents(countriesFilters: String?): ResultWrapper<List<Event>> {
        return safeApiCall {
            client.get(Constants.API.EVENTS) {
                url {
                    if (!countriesFilters.isNullOrEmpty()) {
                        parameters.append("country_filter", countriesFilters)
                    }
                }
            }.body<List<EventDto>>().toVo()
        }
    }

    override suspend fun locate(lat: String, long: String): ResultWrapper<LocationInfos> {

        val res = safeApiCall {
            client.get(Constants.API.LOCATE) {
                url {
                    parameters.append("lat", lat)
                    parameters.append("long", long)
                }
            }.body<LocationInfoResponse>().toVo()
        }
        print(res.flattenIos()?.address)
        return safeApiCall {
            client.get(Constants.API.LOCATE) {
                url {
                    parameters.append("lat", lat)
                    parameters.append("long", long)
                }
            }.body<LocationInfoResponse>().toVo()
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
