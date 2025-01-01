package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.data.model.dto.SectorDto
import com.horionDev.climbingapp.domain.model.composition.BoundingBox

@kotlinx.serialization.Serializable
data class Crag(
    val id: Int = 0,
    val name: String = "",
    val description: String? = null,
    val areaId: Int = 0,
    val sectors: List<SectorDto> = emptyList(),
    val latitude: Double = 44.51,
    val longitude: Double = 5.93,
    val image: String? = "",
    val orientation: String? = null,
    val altitude: String? = null,
    val approachLenght: String? = null,
    val boundingBox: BoundingBox = BoundingBox(0.0, 0.0, 0.0, 0.0),
)

fun Crag.createRoutesJsonForUnity(): String {
    val routes = sectors.flatMap { it.routes.orEmpty() }
    val routesJsonArray = routes.map { route ->
        """
        {
          "routeName": "${route.name}",
          "grade": "${route.grade}",
          "style": "sports",
          "sectorName": "sektor"
        }
        """
    }

    return """
    {
      "routes": [
        ${routesJsonArray.joinToString(separator = ",")}
      ]
    }
    """.trimIndent()
}
