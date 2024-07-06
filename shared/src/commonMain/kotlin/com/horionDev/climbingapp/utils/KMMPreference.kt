package com.horionDev.climbingapp.utils

class KMMPreference(private val context: KMMContext) {

    fun put(key: String, value: Int) =
        context.putInt(key, value)

    fun put(key: String, value: String) =
        context.putString(key, value)

    fun put(key: String, value: Boolean) =
        context.putBool(key, value)

    fun put(key: String, value: Long) =
        context.putLong(key, value)

    fun getLong(key: String, default: Long): Long = context.getLong(key, default)

    fun getInt(key: String, default: Int): Int = context.getInt(key, default)

    fun getString(key: String): String? = context.getString(key)

    fun getBool(key: String, default: Boolean): Boolean =
        context.getBool(key, default)

    fun contains(key: String): Boolean = context.contains(key)

    fun remove(key: String) = context.remove(key)

}