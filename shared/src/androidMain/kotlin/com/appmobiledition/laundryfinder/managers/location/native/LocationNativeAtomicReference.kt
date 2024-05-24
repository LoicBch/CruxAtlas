package com.appmobiledition.laundryfinder.managers.location.native

import java.util.concurrent.atomic.AtomicReference

internal actual class LocationNativeAtomicReference<T> actual constructor(value: T) {

    private val atomic = AtomicReference(value)

    actual var value: T
        get() = atomic.get()
        set(value) = atomic.set(value)
}