package com.appmobiledition.laundryfinder.data.datasources.remote

import com.appmobiledition.laundryfinder.data.ResultWrapper
import com.appmobiledition.laundryfinder.data.map
import com.appmobiledition.laundryfinder.data.model.ErrorMessage
import com.appmobiledition.laundryfinder.data.model.dto.LaundryDto
import com.appmobiledition.laundryfinder.data.model.responses.SuggestionResponse
import com.appmobiledition.laundryfinder.data.safeGet
import com.appmobiledition.laundryfinder.domain.model.Place
import com.appmobiledition.laundryfinder.domain.model.composition.Location
import com.appmobiledition.laundryfinder.utils.Globals
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import toPlaces

fun URLBuilder.addAppContext() {
    parameters.append("ctx_country", Globals.geoLoc.deviceCountry)
    parameters.append("ctx_language", Globals.geoLoc.deviceLanguage)
    parameters.append("ctx_app_language", Globals.geoLoc.appLanguage)
}

class CamperProApi(private var client: HttpClient) : Api {

    override suspend fun getLaudry(
        latitude: Double,
        longitude: Double
    ): ResultWrapper<List<LaundryDto>, ErrorMessage> {
        return client.safeGet {
            url("https://vanlifers.org/laundries.php")
            url {
                parameters.append("latitude", latitude.toString())
                parameters.append("longitude", longitude.toString())
            }
        }
    }

    override suspend fun getLocationSuggestions(input: String): ResultWrapper<List<Place>, ErrorMessage> {
        return client.safeGet<SuggestionResponse, ErrorMessage> {
            url("https://camper-pro.com/services/v4/geocoding.php")
            url {
                parameters.append("q", input)
            }
        }.map {
            it.toPlaces()
        }
    }
}

fun SuggestionResponse.toPlaces() = results.map { Place(it.name, Location(it.lat, it.lng)) }
