package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import com.horionDev.climbingapp.domain.model.entities.FrenchGrade
import com.horionDev.climbingapp.domain.model.entities.UIAAGrade
import com.horionDev.climbingapp.domain.model.entities.YDSGrade
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteParcel(
    val id: Int = 0,
    val name: String = "",
    val cragName: String = "",
    val description: String = "",
    val grade: String = "",
    val area: String = "",
    val sector: String = "",
    val image: String = ""
): Parcelable

enum class GradeNotation(val displayValue: String) {
    French("French"), YDS("YDS"), UIAA("UIAA")
}

fun GradeNotation.getGrade(): List<String> {
    return when (this) {
        GradeNotation.French -> FrenchGrade.entries.map { it.grade }
        GradeNotation.YDS -> YDSGrade.entries.map { it.grade }
        GradeNotation.UIAA -> UIAAGrade.entries.map { it.grade }
    }
}