package com.example.camperpro.android.components

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.domain.model.Location
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.*

class DeferredViewModel<T>(
    private val deferred: Deferred<T>,
    private val viewModelScope: CoroutineScope
) {
    infix fun then(block: (T) -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            block(deferred.await())
        }
    }
}

fun <T> ViewModel.loadAsync(action: suspend () -> T): DeferredViewModel<T> {
    val deferred = viewModelScope.async(
        Dispatchers.IO,
        CoroutineStart.LAZY
    ) {
        action()
    }
    return DeferredViewModel(deferred, viewModelScope)
}

val CameraPositionState.locationVo
    get() = Location(
        this.position.target.latitude,
        this.position.target.longitude
    )

val Context.hasLocationPermission get()  =
    ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

