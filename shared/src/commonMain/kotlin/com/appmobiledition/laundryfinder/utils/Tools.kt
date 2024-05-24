package com.appmobiledition.laundryfinder.utils

import kotlin.math.roundToInt

fun Double.fromKmToMiles() = (this * 0.621371 * 10.0).roundToInt() / 10.0