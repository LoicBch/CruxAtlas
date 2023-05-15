package com.example.camperpro.utils

import platform.Foundation.NSUserDefaults

actual fun KMMContext.putInt(key: String, value: Int) {
    NSUserDefaults.standardUserDefaults.setInteger(value.toLong(), key)
}

actual fun KMMContext.getInt(key: String, default: Int): Int {
    return NSUserDefaults.standardUserDefaults.integerForKey(key).toInt()
}

// TODO: implement for ios
actual fun KMMContext.getLong(key: String, default: Long): Long {
    return 33
}

// TODO: implement for ios
actual fun KMMContext.putLong(key: String, value: Long) {
    NSUserDefaults.standardUserDefaults.setObject(value, key)
}

actual fun KMMContext.putString(key: String, value: String) {
    NSUserDefaults.standardUserDefaults.setObject(value, key)
}

actual fun KMMContext.getString(key: String): String? {
    return NSUserDefaults.standardUserDefaults.stringForKey(key)
}

actual fun KMMContext.putBool(key: String, value: Boolean) {
    NSUserDefaults.standardUserDefaults.setBool(value, key)
}

actual fun KMMContext.getBool(key: String, default: Boolean): Boolean {
    return NSUserDefaults.standardUserDefaults.boolForKey(key)
}

actual fun KMMContext.remove(key: String) {
    NSUserDefaults.standardUserDefaults.removeObjectForKey(key)
}

actual fun KMMContext.contains(key: String): Boolean {
    return true
}