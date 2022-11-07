package com.example.camperpro

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform