package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import com.horionDev.climbingapp.domain.model.entities.FrenchGrade
import com.horionDev.climbingapp.domain.model.entities.UIAAGrade
import com.horionDev.climbingapp.domain.model.entities.YDSGrade
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class RouteGradeParcel : Parcelable {
    abstract val grade: String
    val values
        get() = when (this) {
            is French -> FrenchGrade.values()
            is YDS -> YDSGrade.values()
            is UIAA -> UIAAGrade.values()
        }

    @Parcelize
    data class French(val frenchGrade: FrenchGrade) : RouteGradeParcel(), Parcelable {
        override val grade: String = frenchGrade.grade
    }

    @Parcelize
    data class YDS(val ydsGrade: YDSGrade) : RouteGradeParcel(), Parcelable {
        override val grade: String = ydsGrade.grade
    }

    @Parcelize
    data class UIAA(val uiaaGrade: UIAAGrade) : RouteGradeParcel(), Parcelable {
        override val grade: String = uiaaGrade.grade
    }
}
