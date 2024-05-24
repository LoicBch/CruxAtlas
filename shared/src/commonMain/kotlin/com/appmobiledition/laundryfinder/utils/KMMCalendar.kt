package com.appmobiledition.laundryfinder.utils

class KMMCalendar(private val context: KMMContext) {
    fun getCurrentTime(): Long = context.getCurrentTime()
}