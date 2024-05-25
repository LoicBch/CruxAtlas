package com.appmobiledition.laundryfinder.data.model.dto

import com.appmobiledition.laundryfinder.domain.model.PhotoX
import com.appmobiledition.laundryfinder.utils.CommonParcelable
import com.appmobiledition.laundryfinder.utils.CommonParcelize
import io.ktor.http.*
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