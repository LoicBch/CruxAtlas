package com.appmobiledition.laundryfinder.managers.network.native

internal expect class NetworkNativeAtomicReference<T>(value: T) {
    var value: T
}