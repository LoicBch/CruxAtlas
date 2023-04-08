package com.example.camperpro.managers.location.native

import kotlinx.atomicfu.atomic
import kotlin.native.concurrent.freeze

internal actual class LocationNativeAtomicReference<T> actual constructor(value: T) {

    private val atomic = atomic(value)

    actual var value: T
        get() = atomic.value
        set(value) { atomic.value = value }

    init {
        freeze()
    }
}