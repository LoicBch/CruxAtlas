package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import com.horionDev.climbingapp.domain.model.entities.RouteGrade
import kotlinx.parcelize.Parcelize

@Parcelize
class BoulderParcel(
    var id: Int,
    var cragId: Int,
    var cragName: String,
    var sectorId: Int,
    var name: String,
    var grade: RouteGradeParcel,
): Parcelable