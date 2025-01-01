package com.horionDev.climbingapp.domain.model.entities

data class Model(
    val id: String,
    val name: String,
    val sectorNames: List<String>,
    val routesCount: Int,
    val downloadedDate: String? = null,
    val parentCrag: String = "",
    val size: Long = 0,
//    remove that whe we share ios/android and do the creation of file in repository
    val data: ByteArray? = null
)