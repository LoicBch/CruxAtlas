package com.example.camperpro.android.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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