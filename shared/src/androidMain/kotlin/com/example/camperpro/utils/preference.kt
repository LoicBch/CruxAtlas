package com.example.camperpro.utils

const val SP_NAME = "camperPro_pref"

actual fun KMMContext.putInt(key: String, value: Int) {
    getSpEditor().putInt(key, value).apply()
}

actual fun KMMContext.getInt(key: String, default: Int): Int {
    return getSp().getInt(key, default)
}

actual fun KMMContext.getLong(key: String, default: Long): Long {
    return getSp().getLong(key, default)
}

actual fun KMMContext.putLong(key: String, value: Long) {
    getSpEditor().putLong(key, value).apply()
}

actual fun KMMContext.putString(key: String, value: String) {
    getSpEditor().putString(key, value).apply()
}

actual fun KMMContext.getString(key: String): String? {
    return getSp().getString(key, null)
}

actual fun KMMContext.putBool(key: String, value: Boolean) {
    getSpEditor().putBoolean(key, value).apply()
}

actual fun KMMContext.getBool(key: String, default: Boolean): Boolean {
    return getSp().getBoolean(key, default)
}

actual fun KMMContext.contains(key: String): Boolean = getSp().contains(key)

actual fun KMMContext.remove(key: String) {
    getSpEditor().remove(key)
}

private fun KMMContext.getSp() = getSharedPreferences(SP_NAME, 0)

private fun KMMContext.getSpEditor() = getSp().edit()