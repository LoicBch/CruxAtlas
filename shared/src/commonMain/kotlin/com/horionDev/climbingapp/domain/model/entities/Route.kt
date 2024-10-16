package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize
import com.horionDev.climbingapp.utils.KMMPreference
import kotlinx.serialization.Serializable

@Serializable
@CommonParcelize
data class Route(
    val id: Int = 0,
    val name: String = "",
    val cragName: String = "",
    val description: String = "",
    val grade: String = "",
    val area: String = "",
    val sector: String = "",
    val image: String = ""
) : CommonParcelable

enum class GradeNotation(val displayValue: String) {
    French("French"), YDS("YDS"), UIAA("UIAA")
}

fun GradeNotation.getGrade(): List<String> {
    return when (this) {
        GradeNotation.French -> FrenchGrade.values().map { it.grade }
        GradeNotation.YDS -> YDSGrade.values().map { it.grade }
        GradeNotation.UIAA -> UIAAGrade.values().map { it.grade }
    }
}

@Serializable
sealed class RouteGrade {
    abstract val grade: String
    val values get() = when (this) {
        is French -> FrenchGrade.values()
        is YDS -> YDSGrade.values()
        is UIAA -> UIAAGrade.values()
    }

    @Serializable
    data class French(val frenchGrade: FrenchGrade) : RouteGrade() {
        override val grade: String = frenchGrade.grade
    }

    @Serializable
    data class YDS(val ydsGrade: YDSGrade) : RouteGrade() {
        override val grade: String = ydsGrade.grade
    }

    @Serializable
    data class UIAA(val uiaaGrade: UIAAGrade) : RouteGrade() {
        override val grade: String = uiaaGrade.grade
    }
}

@Serializable
enum class FrenchGrade(val grade: String) {
    GRADE_1("1"),
    GRADE_2("2"),
    GRADE_2B("2b"),
    GRADE_3("3"),
    GRADE_3B("3b"),
    GRADE_4A("4a"),
    GRADE_4B("4b"),
    GRADE_4C("4c"),
    GRADE_5A("5a"),
    GRADE_5B("5b"),
    GRADE_5C("5c"),
    GRADE_6A("6a"),
    GRADE_6A_PLUS("6a+"),
    GRADE_6B("6b"),
    GRADE_6B_PLUS("6b+"),
    GRADE_6C("6c"),
    GRADE_6C_PLUS("6c+"),
    GRADE_7A("7a"),
    GRADE_7A_PLUS("7a+"),
    GRADE_7B("7b"),
    GRADE_7B_PLUS("7b+"),
    GRADE_7C("7c"),
    GRADE_7C_PLUS("7c+"),
    GRADE_8A("8a"),
    GRADE_8A_PLUS("8a+"),
    GRADE_8B("8b"),
    GRADE_8B_PLUS("8b+"),
    GRADE_8C("8c"),
    GRADE_8C_PLUS("8c+"),
    GRADE_9A("9a"),
    GRADE_9A_PLUS("9a+"),
    GRADE_9B("9b"),
    GRADE_9B_PLUS("9b+"),
    GRADE_9C("9c")
}

@Serializable
enum class UIAAGrade(val grade: String) {
    GRADE_1("1"),
    GRADE_2("2"),
    GRADE_2B("2"),
    GRADE_3("3"),
    GRADE_3B("3"),
    GRADE_4_MINUS("4-"),
    GRADE_4("4"),
    GRADE_4_PLUS("4+"),
    GRADE_5_MINUS("5-"),
    GRADE_5_PLUS("5+"),
    GRADE_6("6"),
    GRADE_6_PLUS("6+"),
    GRADE_7_MINUS("7-"),
    GRADE_7("7"),
    GRADE_7_PLUS("7+"),
    GRADE_7_PLUS_8_MINUS("7+/8-"),
    GRADE_8_MINUS("8-"),
    GRADE_8("8"),
    GRADE_8_PLUS("8+"),
    GRADE_8_PLUS_9_MINUS("8+/9-"),
    GRADE_9_MINUS("9-"),
    GRADE_9("9"),
    GRADE_9_PLUS("9+"),
    GRADE_9_PLUS_10_MINUS("9+/10-"),
    GRADE_10_MINUS("10-"),
    GRADE_10("10"),
    GRADE_10_PLUS("10+"),
    GRADE_10_PLUS_11_MINUS("10+/11-"),
    GRADE_11_MINUS("11-"),
    GRADE_11("11"),
    GRADE_11_PLUS("11+"),
    GRADE_11_PLUS_12_MINUS("11+/12-"),
    GRADE_12_MINUS("12-"),
    GRADE_12("12")
}

@Serializable
enum class YDSGrade(val grade: String) {
    GRADE_5_0("5.0"),
    GRADE_5_1("5.1"),
    GRADE_5_2("5.2"),
    GRADE_5_3("5.3"),
    GRADE_5_4("5.4"),
    GRADE_5_5("5.5"),
    GRADE_5_6("5.6"),
    GRADE_5_7("5.7"),
    GRADE_5_8("5.8"),
    GRADE_5_9("5.9"),
    GRADE_5_10_A("5.10a"),
    GRADE_5_10_B("5.10b"),
    GRADE_5_10_C("5.10c"),
    GRADE_5_10_D("5.10d"),
    GRADE_5_11_A("5.11a"),
    GRADE_5_11_B("5.11b"),
    GRADE_5_11_C("5.11c"),
    GRADE_5_11_D("5.11d"),
    GRADE_5_12_A("5.12a"),
    GRADE_5_12_B("5.12b"),
    GRADE_5_12_C("5.12c"),
    GRADE_5_12_D("5.12d"),
    GRADE_5_13_A("5.13a"),
    GRADE_5_13_B("5.13b"),
    GRADE_5_13_C("5.13c"),
    GRADE_5_13_D("5.13d"),
    GRADE_5_14_A("5.14a"),
    GRADE_5_14_B("5.14b"),
    GRADE_5_14_C("5.14c"),
    GRADE_5_14_D("5.14d"),
    GRADE_5_15_A("5.15a"),
    GRADE_5_15_B("5.15b"),
    GRADE_5_15_C("5.15c"),
    GRADE_5_15_D("5.15d")
}
