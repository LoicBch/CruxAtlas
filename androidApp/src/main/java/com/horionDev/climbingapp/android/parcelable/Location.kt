package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import com.horionDev.climbingapp.utils.*
import kotlinx.parcelize.Parcelize
import kotlin.math.*

@Parcelize
class LocationParcel(val latitude: Double, val longitude: Double) : Parcelable