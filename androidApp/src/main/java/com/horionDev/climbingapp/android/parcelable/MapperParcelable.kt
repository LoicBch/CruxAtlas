package com.horionDev.climbingapp.android.parcelable

import CragDetailsDto
import com.horionDev.climbingapp.data.model.dto.ParkingSpotDto
import com.horionDev.climbingapp.domain.model.composition.Place
import com.horionDev.climbingapp.domain.model.composition.BoundingBox
import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.domain.model.entities.Area
import com.horionDev.climbingapp.domain.model.entities.Boulder
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Model
import com.horionDev.climbingapp.domain.model.entities.Route
import com.horionDev.climbingapp.domain.model.entities.RouteGrade
import com.horionDev.climbingapp.domain.model.entities.Sector
import com.horionDev.climbingapp.domain.model.entities.User
import toDto
import toVo

fun Area.toParcelable() = AreaParcel(
    id = id,
    name = name,
    description = description,
    polygon = polygon.map { Pair(it.latitude, it.longitude) }
)

fun AreaParcel.fromParcelable() = Area(
    id = id,
    name = name,
    description = description,
    country = "",
    polygon = polygon.map { Location(it.first, it.second) }
)

fun Boulder.toParcelable() = BoulderParcel(
    id = id,
    name = name,
    cragId = cragId,
    cragName = cragName,
    sectorId = sectorId,
    grade = grade.toParcelable(),
)

fun BoulderParcel.fromParcelable() = Boulder(
    id = id,
    name = name,
    cragId = cragId,
    cragName = cragName,
    sectorId = sectorId,
    grade = grade.fromParcelable(),
)

fun Crag.toParcelable() = CragParcel(
    id = id,
    name = name,
    description = description,
    areaId = areaId,
    sectors = sectors.map { it.toVo().toParcelable() },
    image = image,
    latitude = latitude,
    longitude = longitude,
    orientation = orientation,
    altitude = altitude,
    approachLenght = approachLenght,
    boundingBox = boundingBox.toParcelable()
)

fun CragParcel.fromParcelable() = Crag(
    id = id,
    name = name,
    description = description,
    areaId = areaId,
    sectors = sectors.map { it.fromParcelable().toDto() },
    image = image,
    latitude = latitude,
    longitude = longitude,
    orientation = orientation,
    altitude = altitude,
    approachLenght = approachLenght,
    boundingBox = boundingBox.fromParcelable()
)

fun Sector.toParcelable() = SectorParcel(
    id = id,
    name = name,
    description = description,
    cragId = cragId,
    routes = routes.map { it.toParcelable() },
    parkingSpots = parkingSpots?.map { it.toParcelable() },
    boundingBox = boundingBox.toParcelable(),
    image = image
)

fun SectorParcel.fromParcelable() = Sector(
    id = id,
    name = name,
    description = description,
    cragId = cragId,
    routes = routes.map { it.fromParcelable() },
    parkingSpots = parkingSpots?.map { it.fromParcelable() },
    boundingBox = boundingBox.fromParcelable(),
    image = image
)

fun BoundingBox.toParcelable() = BoundingBoxParcel(
    minLatitude = minLatitude,
    minLongitude = minLongitude,
    maxLatitude = maxLatitude,
    maxLongitude = maxLongitude
)

fun BoundingBoxParcel.fromParcelable() = BoundingBox(
    minLatitude = minLatitude,
    minLongitude = minLongitude,
    maxLatitude = maxLatitude,
    maxLongitude = maxLongitude
)

fun ParkingSpotDto.toParcelable() = ParkingSpotDtoParcel(
    name = name,
    description = description,
    latitude = latitude,
    longitude = longitude
)

fun ParkingSpotDtoParcel.fromParcelable() = ParkingSpotDto(
    name = name,
    description = description,
    latitude = latitude,
    longitude = longitude
)

fun Route.toParcelable() = RouteParcel(
    id = id,
    name = name,
    grade = grade,
    description = description,
    area = area,
    sector = sector,
    image = image
)

fun RouteParcel.fromParcelable() = Route(
    id = id,
    name = name,
    grade = grade,
    description = description,
    area = area,
    sector = sector,
    image = image
)

fun User.toParcelable() = UserParcel(
    id = id,
    username = username,
    password,
    email,
    country,
    city,
    gender,
    age,
    weight,
    height,
    climbingSince,
    statut,
    isSubscribe,
    favorites,
    imageUrl
)

fun UserParcel.fromParcelable() = User(
    id = id,
    username = username,
    password,
    email,
    country,
    city,
    gender,
    age,
    weight,
    height,
    climbingSince,
    statut,
    isSubscribe,
    favorites,
    imageUrl
)

fun RouteGrade.toParcelable(): RouteGradeParcel {
    return when (this) {
        is RouteGrade.French -> RouteGradeParcel.French(this.frenchGrade)
        is RouteGrade.YDS -> RouteGradeParcel.YDS(this.ydsGrade)
        is RouteGrade.UIAA -> RouteGradeParcel.UIAA(this.uiaaGrade)
    }
}

fun RouteGradeParcel.fromParcelable(): RouteGrade {
    return when (this) {
        is RouteGradeParcel.French -> RouteGrade.French(this.frenchGrade)
        is RouteGradeParcel.YDS -> RouteGrade.YDS(this.ydsGrade)
        is RouteGradeParcel.UIAA -> RouteGrade.UIAA(this.uiaaGrade)
    }
}

fun CragDetailsDto.toParcelable() = CradDetailsParcel(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    thumbnailUrl = thumbnailUrl,
    sectors = sectors.map { it.toVo()!!.toParcelable() },
    models = models.map { it.toVo()!!.toParcelable() }
)

fun CradDetailsParcel.fromParcelable() = CragDetailsDto(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    thumbnailUrl = thumbnailUrl,
    sectors = sectors.map { it.fromParcelable().toDto() },
    models = models.map { it.fromParcelable().toDto() }
)

fun ModelParcel.fromParcelable() = Model(
    id = id,
    name = name,
    sectorNames = sectorNames,
    routesCount = routesCount,
    downloadedDate = downloadedDate,
    parentCrag = parentCrag,
    size = size,
    data = data
)

fun Model.toParcelable() = ModelParcel(
    id = id,
    name = name,
    sectorNames = sectorNames,
    routesCount = routesCount,
    downloadedDate = downloadedDate,
    parentCrag = parentCrag,
    size = size,
    data = data
)

fun Location.toParcelable() = LocationParcel(
    latitude = latitude,
    longitude = longitude
)

fun LocationParcel.fromParcelable() = Location(
    latitude = latitude,
    longitude = longitude
)

fun Place.toParcelable() = PlaceParcel(
    name = name,
    location = location.toParcelable(),
)

fun PlaceParcel.fromParcelable() = Place(
    name = name,
    location = location.fromParcelable(),
)
