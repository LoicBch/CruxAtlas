package com.horionDev.climbingapp.data.model.responses

import com.horionDev.climbingapp.data.model.dto.MenuLinkDto
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class StarterResponse(

    @SerialName("status")
    val status: String = "",

    @SerialName("lists")
    val lists: Lists

)

@kotlinx.serialization.Serializable
data class Lists(
    @SerialName("brand_mh")
    val brands: List<Brand>,

    @SerialName("service_mh")
    val services: List<Service>,

    @SerialName("menu_link")
    val menuLinks: List<MenuLinkDto>
)

@kotlinx.serialization.Serializable
data class Brand(
    val id: String,
    val name: String
)

@kotlinx.serialization.Serializable
data class Service(
    val id: String,
    val label: String,
    val rank: String
)


