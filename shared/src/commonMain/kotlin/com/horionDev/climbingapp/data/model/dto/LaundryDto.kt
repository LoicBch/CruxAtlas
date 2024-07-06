package com.horionDev.climbingapp.data.model.dto

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
@CommonParcelize
data class LaundryDto(
    val id: String?,
    val name: String?,
    val description: String?,
    val city: String?,
    val zipcode: String?,
    val region: String?,
    @SerialName("country_iso")
    val country: String?,
    val created_by : String?,
    val created_at : String?,
    val website: String?,
    val phone : String?,
    val email : String?,
    @SerialName("lat")
    val latitude: String?,
    @SerialName("lng")
    val longitude: String?
) : CommonParcelable

@kotlinx.serialization.Serializable
@CommonParcelize
data class PhotoDto(
    val id: String?,
    val link_large: String?,
    val link_thumb: String?,
    val numero: String?,
    val p4n_user_id: String?,
    val pn_lieu_id: String?
): CommonParcelable