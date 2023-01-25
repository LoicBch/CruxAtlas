package com.example.camperpro.managers.location.native

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect class NativeAtomicReference<T>(value: T) {
    var value: T
}