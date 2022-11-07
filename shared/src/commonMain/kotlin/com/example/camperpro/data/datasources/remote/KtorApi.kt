package com.jetbrains.kmm.shared.data.datasources.remote

import io.ktor.client.*
import io.ktor.client.engine.cio.*
//import io.ktor.client.plugins.logging.*

object KtorApi {

    val client = HttpClient(CIO) {
//        install(Logging)
    }



}