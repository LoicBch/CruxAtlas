package com.horionDev.climbingapp.data.datasources.remote

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.map
import com.horionDev.climbingapp.data.model.ErrorMessage
import com.horionDev.climbingapp.data.model.dto.LaundryDto
import com.horionDev.climbingapp.data.model.responses.SuggestionResponse
import com.horionDev.climbingapp.data.safeGet
import com.horionDev.climbingapp.domain.model.Place
import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.utils.Globals
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import toPlaces

fun URLBuilder.addAppContext() {
    parameters.append("ctx_country", Globals.GeoLoc.deviceCountry)
    parameters.append("ctx_language", Globals.GeoLoc.deviceLanguage)
    parameters.append("ctx_app_language", Globals.GeoLoc.appLanguage)
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
