package com.appmobiledition.laundryfinder.managers.location.native

internal expect class LocationNativeAtomicReference<T>(value: T) {
    var value: T
}