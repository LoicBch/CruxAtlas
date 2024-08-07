package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.domain.model.composition.BoundingBox
import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize

@kotlinx.serialization.Serializable
@CommonParcelize
data class Crag(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val areaId: Int = 0,
    val sectors: List<Sector> = emptyList(),
    val latitude: Double = 44.51,
    val longitude: Double = 5.93,
    val boundingBox: BoundingBox = BoundingBox(0.0, 0.0, 0.0, 0.0),
    val image: String = "",
    val orientation: String? = "South",
    val altitude: String? = "2016m",
    val approachLenght: String? = "30min"
) : CommonParcelable

val demiLuneRoutes = listOf(
    Route(
        name = "Lapinerie", grade = RouteGrade.SevenBPlus
    ), Route(
        name = "La javanaise", grade = RouteGrade.SevenA
    ), Route(
        name = "Cartes blanche", grade = RouteGrade.EightA
    ), Route(
        name = "Angel Dust", grade = RouteGrade.SevenAPlus
    ), Route(
        name = "Melody Nelson", grade = RouteGrade.SevenAPlus
    ), Route(
        name = "Harley Davidson", grade = RouteGrade.SixBPlus
    ), Route(
        name = "La femme noire", grade = RouteGrade.SevenCPlus
    ), Route(
        name = "Marylou", grade = RouteGrade.SixB
    ), Route(
        name = "Changement de look", grade = RouteGrade.SevenBPlus
    )
)

val berlinRoutes = listOf(
    Route(
        name = "la petite illusion", grade = RouteGrade.SevenAPlus
    ), Route(
        name = "Blocage violent", grade = RouteGrade.SevenBPlus
    ), Route(
        name = "Zagreb", grade = RouteGrade.SixC
    ), Route(
        name = "Berlin", grade = RouteGrade.SevenC
    ), Route(
        name = "100 Patates", grade = RouteGrade.SevenB
    ), Route(
        name = "San jones pecos", grade = RouteGrade.SevenBPlus
    ), Route(
        name = "Super Mario", grade = RouteGrade.SixBPlus
    ), Route(
        name = "Petit Tom", grade = RouteGrade.EightA
    ), Route(
        name = "Galaxy", grade = RouteGrade.SevenBPlus
    )
)

val cascadeRoutes = listOf(
    Route(
        name = "Ananda", grade = RouteGrade.SevenA
    ), Route(
        name = "Vagabond d'occident", grade = RouteGrade.SevenC
    ), Route(
        name = "Super Mickey", grade = RouteGrade.SevenBPlus
    ), Route(
        name = "Le privilege du serpent", grade = RouteGrade.SevenCPlus
    ), Route(
        name = "Mirage", grade = RouteGrade.SevenCPlus
    ), Route(
        name = "Corps etranger", grade = RouteGrade.SevenBPlus
    ), Route(
        name = "Medecine douce", grade = RouteGrade.SixCPlus
    ), Route(
        name = "Blanches fesses", grade = RouteGrade.SevenC
    ), Route(
        name = "Tenere", grade = RouteGrade.SevenCPlus
    )
)

val unPontSurLinfiniRoutes = listOf(
    Route(
        name = "La reine des pommes", grade = RouteGrade.SixCPlus
    ), Route(
        name = "Un pont sur l'infini", grade = RouteGrade.SevenA
    ), Route(
        name = "Dietetic line", grade = RouteGrade.SevenB
    ), Route(
        name = "Vas y tonton", grade = RouteGrade.SevenA
    ), Route(
        name = "Cheyenne automne", grade = RouteGrade.SevenAPlus
    ), Route(
        name = "2001 l'odyssee du grimpeur", grade = RouteGrade.SevenB
    ), Route(
        name = "Gelati Dominiti", grade = RouteGrade.SevenA
    ), Route(
        name = "La galere", grade = RouteGrade.SevenA
    ), Route(
        name = "Opera vertical", grade = RouteGrade.SevenB
    )
)

val grandeFaceRoutes = listOf(
    Route(
        name = "Ange", grade = RouteGrade.SevenA
    ), Route(
        name = "100% Collegue", grade = RouteGrade.SevenAPlus
    ), Route(
        name = "A trop tater ton trou tu t'irrite", grade = RouteGrade.SixC
    ), Route(
        name = "Les volets bleus", grade = RouteGrade.SevenA
    ), Route(
        name = "Makhnovitvhina", grade = RouteGrade.SevenBPlus
    ), Route(
        name = "Trous line", grade = RouteGrade.SixBPlus
    ), Route(
        name = "Tabernacle", grade = RouteGrade.SixB
    ), Route(
        name = "L'ete Ceusien", grade = RouteGrade.SevenA
    ), Route(
        name = "Papy Boume", grade = RouteGrade.SevenBPlus
    )
)

val beauMouvementRoutes = listOf(
    Route(
        name = "Bourinator", grade = RouteGrade.EightA
    ), Route(
        name = "Pourquoi pas ?", grade = RouteGrade.SevenAPlus
    ), Route(
        name = "Au sud de nulle part", grade = RouteGrade.SevenB
    ), Route(
        name = "Beau mouvement", grade = RouteGrade.SevenAPlus
    ), Route(
        name = "Machoire d'ane", grade = RouteGrade.SevenC
    ), Route(
        name = "Le coeur des hommes", grade = RouteGrade.SevenA
    ), Route(
        name = "A l'ouest de partout", grade = RouteGrade.SevenA
    ), Route(
        name = "Desert mineral", grade = RouteGrade.SevenB
    ), Route(
        name = "Slow food", grade = RouteGrade.EightBPlus
    )
)

val biographyRoutes = listOf(
    Route(
        name = "Saint Goerge Picos", grade = RouteGrade.SevenA
    ), Route(
        name = "Nitassinan", grade = RouteGrade.SevenA
    ), Route(
        name = "Les colonnettes", grade = RouteGrade.SevenCPlus
    ), Route(
        name = "J'aime Lolo T", grade = RouteGrade.SevenA
    ), Route(
        name = "Tout n'est pas si facile", grade = RouteGrade.SevenCPlus
    ), Route(
        name = "Sitting bull", grade = RouteGrade.SevenBPlus
    ), Route(
        name = "Woundeed knee", grade = RouteGrade.SevenB
    ), Route(
        name = "Chronique de la haine ordinaire", grade = RouteGrade.EightC
    ), Route(
        name = "Black bean", grade = RouteGrade.EightB
    )
)

val demiLune = Sector(
    name = "Demi Lune",
    routes = demiLuneRoutes,
    boundingBox = BoundingBox(44.484945, 5.937846, 44.484945, 5.937846),
    image = "https://mountainproject.com/assets/photos/climb/106837729_medium_1494144526.jpg?cache=1701316443"
)

val berlin = Sector(
    name = "Berlin",
    routes = berlinRoutes,
    boundingBox = BoundingBox(44.484945, 5.937846, 44.484945, 5.937846),
    image = "https://img.over-blog-kiwi.com/0/37/82/35/20140603/ob_c083f4_p1060141.JPG"
)

val cascade = Sector(
    name = "Cascade",
    routes = cascadeRoutes,
    boundingBox = BoundingBox(44.484945, 5.937846, 44.484945, 5.937846),
    image = "https://img.over-blog-kiwi.com/0/37/82/35/20140603/ob_c083f4_p1060141.JPG"
)

val unPontSurLinfini = Sector(
    name = "Un pont sur l'infinit",
    routes = unPontSurLinfiniRoutes,
    boundingBox = BoundingBox(44.484945, 5.937846, 44.484945, 5.937846),
    image = "https://mountainproject.com/assets/photos/climb/106837729_medium_1494144526.jpg?cache=1701316443"
)

val grandeFace = Sector(
    name = "Grande Face",
    routes = grandeFaceRoutes,
    boundingBox = BoundingBox(44.484945, 5.937846, 44.484945, 5.937846),
    image = "https://mountainproject.com/assets/photos/climb/106837729_medium_1494144526.jpg?cache=1701316443"
)

val beauMouvement = Sector(
    name = "Beau mouvement",
    routes = beauMouvementRoutes,
    boundingBox = BoundingBox(44.484945, 5.937846, 44.484945, 5.937846),
    image = "https://img.over-blog-kiwi.com/0/37/82/35/20140603/ob_c083f4_p1060141.JPG"
)

val biography = Sector(
    name = "Biography",
    routes = biographyRoutes,
    boundingBox = BoundingBox(44.484945, 5.937846, 44.484945, 5.937846),
    image = "https://upload.wikimedia.org/wikipedia/commons/b/b6/C%C3%A9%C3%BCse_secteurs_Berlin_et_Biographie.jpg"
)

val ceuse = Crag(
    id = 1,
    name = "Ceuse",
    description = "Ceuse is a climbing area in the Hautes-Alpes department of France, situated in the commune of Sigoyer near the town of Gap. It is known for its beautiful limestone cliffs, which rise to 300m in height, and its high-quality climbing routes. The area is popular with climbers from all over the world, and is considered one of the best sport climbing destinations in Europe.",
    areaId = 1,
    sectors = listOf(
        demiLune,
        berlin,
        cascade,
        unPontSurLinfini,
        grandeFace,
        beauMouvement,
        biography
    ),
    boundingBox = BoundingBox(44.484945, 5.937846, 44.484945, 5.937846),
    image = "https://image.thecrag.com/1x117:2321x1335/fit-in/1200x630/e9/9f/e99f6b3a38e59c5fc88cde5fa4b870eb1473a550"
)

fun Crag.gradeDistributionString(): String {
    val gradeDistribution = mutableMapOf<RouteGrade, Int>()
    sectors.forEach { sector ->
        sector.routes.forEach { route ->
            gradeDistribution[route.grade] = (gradeDistribution[route.grade] ?: 0) + 1
        }
    }
    return "From " + RouteGrade.values()[gradeDistribution.minOf { it.key.ordinal }].displayValue + " to " +
            RouteGrade.values()[gradeDistribution
                .maxOf { it.key.ordinal }].displayValue
}