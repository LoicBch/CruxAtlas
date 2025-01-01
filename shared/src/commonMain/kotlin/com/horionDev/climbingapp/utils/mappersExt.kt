import com.horionDev.climbingapp.data.model.dto.AreaDto
import com.horionDev.climbingapp.data.model.dto.BoulderDto
import com.horionDev.climbingapp.data.model.dto.CragDto
import com.horionDev.climbingapp.data.model.dto.ModelDto
import com.horionDev.climbingapp.data.model.dto.NewsItemDto
import com.horionDev.climbingapp.data.model.dto.RouteDto
import com.horionDev.climbingapp.data.model.dto.SectorDto
import com.horionDev.climbingapp.data.model.dto.UserDto
import com.horionDev.climbingapp.data.model.dto.UserProfileDto
import com.horionDev.climbingapp.data.model.responses.SuggestionResponse
import com.horionDev.climbingapp.domain.model.entities.NewsItem
import com.horionDev.climbingapp.domain.model.composition.Place
import com.horionDev.climbingapp.domain.model.entities.UserProfile
import com.horionDev.climbingapp.domain.model.composition.AppMarker
import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.domain.model.entities.Area
import com.horionDev.climbingapp.domain.model.entities.Boulder
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.FrenchGrade
import com.horionDev.climbingapp.domain.model.entities.Model
import com.horionDev.climbingapp.domain.model.entities.Route
import com.horionDev.climbingapp.domain.model.entities.RouteGrade
import com.horionDev.climbingapp.domain.model.entities.Sector
import com.horionDev.climbingapp.domain.model.entities.User
import kotlin.jvm.JvmName

@JvmName(name = "userDtoListToVo")
fun List<UserDto>.toVo() = map { it.toVo() }
fun UserDto.toVo() = User(
    id,
    username,
    password,
    email,
    country,
    city,
    gender, age, weight, height, if (climbingSince == "null") "" else climbingSince,
    "U",
    isSubscribe,
    imageUrl = if (imageUrl == "") null else imageUrl
)

@JvmName(name = "cragDetailsDtoListToVo")
fun List<CragDetailsDto>.toVo() = map { it.toVo() }
fun CragDetailsDto.toVo() = CragDetails(
    id
)

@JvmName(name = "modelListToDto")
fun List<Model>.toDto() = map { it.toDto() }
fun Model.toDto() = ModelDto(
    id,
    name,
    sectorNames,
    routesCount,
    downloadedDate,
    parentCrag,
    size = size,
    data
)

@JvmName(name = "modelDtoListToVo")
fun List<ModelDto>.toVo() = map { it.toVo() }
fun ModelDto.toVo() = Model(
    id,
    name,
    sectorNames,
    routesCount,
    downloadedDate,
    parentCrag,
    size = size,
    data
)

class CragDetails(id: Int) {

}

@JvmName(name = "userProfileDtoListToVo")
fun List<UserProfileDto>.toVo() = map { it.toVo() }
fun UserProfileDto.toVo() = UserProfile(
    username,
    email,
    creationDate
)

@JvmName(name = "newsDtoListToVo")
fun List<NewsItemDto>.toVo() = map { it.toVo() }
fun NewsItemDto.toVo() = NewsItem(
    id,
    title,
    description,
    imageUrl,
//    date
)

@JvmName(name = "cragDtoListToVo")
fun List<CragDto>.toVo() = map { it.toVo() }
fun CragDto.toVo() = Crag(
    id,
    name,
    description,
    areaId.toInt(),
    emptyList(),
    latitude,
    longitude,
    thumbnailUrl
)

@JvmName(name = "sectorDtoListToVo")
fun List<SectorDto>.toVo() = map { it.toVo() }
fun SectorDto.toVo() =
    Sector(
        id,
        name,
        description = "",
        cragId,
        routes = routes?.toVo() ?: emptyList(),
        parkingSpots = parkingSpots
    )

@JvmName(name = "sectorVoListToDto")
fun List<Sector>.toVo() = map { it.toDto() }
fun Sector.toDto() = SectorDto(
    id,
    cragId,
    name,
    routes = routes.toDto(),
    parkingSpots = parkingSpots
)

@JvmName(name = "areaDtoListToVo")
fun List<AreaDto>.toVo() = map { it.toVo() }
fun AreaDto.toVo() = Area(
    id,
    name,
    country,
    polygon = polygon
)

@JvmName(name = "routeDtoListToVo")
fun List<RouteDto>.toVo() = map { it.toVo() }
fun RouteDto.toVo() = Route(
    id,
    name,
    cragName = cragName ?: "",
    description = "",
    grade,
    cragId.toString(),
    sectorId.toString()
)

@JvmName(name = "routeToDto")
fun List<Route>.toDto() = map { it.toDto() }
fun Route.toDto() = RouteDto(
    id = id,
    name = name,
    cragName = cragName,
    grade = grade,
    sectorId = 2,
    cragId = 1,
    sectorName = "sectorName",
    ascents = 1,
    rating = 1.0
)

@JvmName(name = "boulderDtoListToVo")
fun List<BoulderDto>.toVo() = map { it.toVo() }
fun BoulderDto.toVo() = Boulder(
    id,
    cragId,
    cragName,
    sectorId,
    name,
    grade = RouteGrade.French(FrenchGrade.GRADE_1)
)

@JvmName("listClimbingToListMarker")
fun Crag.toMarker() = AppMarker(id.toString(), false, this.latitude, this.longitude)

@JvmName("listClimbingToListMark")
fun List<Crag>.toMarker() = map { it.toMarker() }


fun SuggestionResponse.toPlaces() = results.map { Place(it.name, Location(it.lat, it.lng)) }