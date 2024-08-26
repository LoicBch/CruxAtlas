package com.horionDev.climbingapp.data.datasources.remote

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.map
import com.horionDev.climbingapp.data.model.ErrorMessage
import com.horionDev.climbingapp.data.model.dto.CragDto
import com.horionDev.climbingapp.data.model.responses.SuggestionResponse
import com.horionDev.climbingapp.data.safeGet
import com.horionDev.climbingapp.data.safePost
import com.horionDev.climbingapp.domain.model.Place
import com.horionDev.climbingapp.domain.model.composition.AuthRequest
import com.horionDev.climbingapp.domain.model.composition.AuthResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.User
import com.horionDev.climbingapp.utils.Constants
import com.horionDev.climbingapp.utils.Globals
import com.horionDev.climbingapp.utils.KMMPreference
import com.horionDev.climbingapp.data.model.dto.UserDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import toPlaces
import toVo

fun URLBuilder.addAppContext() {
    parameters.append("ctx_country", Globals.GeoLoc.deviceCountry)
    parameters.append("ctx_language", Globals.GeoLoc.deviceLanguage)
    parameters.append("ctx_app_language", Globals.GeoLoc.appLanguage)
}

fun HttpRequestBuilder.addBearerToken(kmmPreference: KMMPreference) {
    val token = kmmPreference.getString(Constants.Preferences.SESSION_TOKEN)
    this.header(HttpHeaders.Authorization, "Bearer $token")
}

class CruxAtlasApi(private var client: HttpClient) : Api, KoinComponent {

    private val kmmPreference : KMMPreference by inject()

    override suspend fun login(authRequest: AuthRequest): ResultWrapper<AuthResponse, ErrorResponse> {
        return client.safePost<AuthResponse, ErrorResponse> {
            url("login")
            contentType(ContentType.Application.Json)
            setBody(authRequest)
        }
    }

    override suspend fun signup(
        username: String,
        password: String,
        email: String
    ): ResultWrapper<AuthResponse, ErrorResponse> {
       return client.safePost<AuthResponse, ErrorResponse> {
           url("signup")
           contentType(ContentType.Application.Json)
           setBody("")
       }
    }

    override suspend fun authenticate(token: String): ResultWrapper<User, ErrorResponse> {
        return client.safeGet<UserDto, ErrorResponse> {
            url("authenticate")
            addBearerToken(kmmPreference)
        }.map { it.toVo() }
    }

    override suspend fun getCragAroundLocation(
        latitude: Double,
        longitude: Double
    ): ResultWrapper<List<Crag>, ErrorResponse> {
        return client.safeGet<List<CragDto>, ErrorResponse> {
            url("crags")
            url {
                parameters.append("lat", latitude.toString())
                parameters.append("long", longitude.toString())
//                if (userId != null && userId != 0) {
//                    parameters.append("user_id", userId.toString())
//                }
//                if (filter != "") {
//                    parameters.append("filter", filter)
//                }
            }

        }.map { it.toVo() }
    }

    override suspend fun getCragDetails(cragId: Int): ResultWrapper<Crag, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpotAsFavoriteToUser(
        userId: Int,
        spotId: Int
    ): ResultWrapper<List<String>, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun removeSpotAsFavoriteForUser(
        userId: Int,
        spotId: Int
    ): ResultWrapper<List<String>, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpotsFavoriteByUser(userId: Int): ResultWrapper<List<Crag>, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpot(crag: Crag): ResultWrapper<Crag, ErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun get3DModel(cragId: Int): ResultWrapper<String, ErrorResponse> {
        TODO("Not yet implemented")
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
