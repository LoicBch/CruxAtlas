package com.appmobiledition.laundryfinder.domain.model

data class Search(
    val categoryKey: String,
    val searchLabel: String,
    val timeStamp: Long,
    val lat: Double? = null,
    val lon: Double? = null
)

