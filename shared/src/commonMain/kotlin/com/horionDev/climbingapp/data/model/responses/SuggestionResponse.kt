package com.horionDev.climbingapp.data.model.responses

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SuggestionResponse(
    val status: String,
    val results: List<Result>,
    val cache: Boolean,
    val source: String,

    @SerialName("seach_lang")
    val seachLang: String,

    @SerialName("exit_lang")
    val exitLang: String,
    val url: String
)

@kotlinx.serialization.Serializable
data class Result(

    @SerialName("osm_id")
    val osmID: Long,
    val lat: Double,
    val lng: Double,
    val name: String,

    @SerialName("place_rank")
    val placeRank: Int? = null
)