package com.example.camperpro.utils

import com.example.camperpro.domain.model.Location
import kotlinx.coroutines.Job

class Globals {
    companion object {
        var lastKnownLocation: Location? = null
        var locationObserver: Job? = null
    }
}