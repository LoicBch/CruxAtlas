package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize

@kotlinx.serialization.Serializable
@CommonParcelize
data class Route(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val grade: RouteGrade,
    val area: String = "",
    val sector: String = "",
    val image: String = ""
): CommonParcelable

enum class RouteGrade(val displayValue: String) {
    One("1"), Two("2"), TwoPlus("2+"), ThreeA("3A"), ThreeB("3B"), ThreeC("3C"),
    FourA("4A"), FourB("4B"), FourC("4C"), FiveA("5A"), FiveB("5B"), FiveC("5C"),
    SixA("6A"), SixAPlus("6A+"), SixB("6B"), SixBPlus("6B+"), SixC("6C"), SixCPlus("6C+"),
    SevenA("7A"), SevenAPlus("7A+"), SevenB("7B"), SevenBPlus("7B+"), SevenC("7C"), SevenCPlus("7C+"),
    EightA("8A"), EightAPlus("8A+"), EightB("8B"), EightBPlus("8B+"), EightC("8C"), EightCPlus("8C+"),
    NineA("9A"), NineAPlus("9A+"), NineB("9B"), NineBPlus("9B+"), NineC("9C"), NineCPlus("9C+")
}