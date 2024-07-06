package com.horionDev.climbingapp.utils

class KMMCalendar(private val context: KMMContext) {
    fun getCurrentTime(): Long = context.getCurrentTime()
}