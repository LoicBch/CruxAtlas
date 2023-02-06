package com.example.camperpro.managers.network.native

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect class NativeAtomicReference<T>(value: T) {
    var value: T
}