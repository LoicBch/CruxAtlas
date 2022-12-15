package com.example.camperpro.android.components

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.android.dataStore
import com.example.camperpro.utils.Globals
import com.example.camperpro.domain.model.Location
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

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

fun Context.checkPermission(identifier: String) =
    ContextCompat.checkSelfPermission(this, identifier) == PackageManager.PERMISSION_GRANTED

suspend fun Context.readAllKeys(): Set<Preferences.Key<*>>? {
    val keys = dataStore.data
        .map {
            it.asMap().keys
        }
    return keys.firstOrNull()
}

suspend fun Context.getValueByKey(key: Preferences.Key<*>): Any? {
    val value = dataStore.data
        .map {
            it[key]
        }
    return value.firstOrNull()
}

