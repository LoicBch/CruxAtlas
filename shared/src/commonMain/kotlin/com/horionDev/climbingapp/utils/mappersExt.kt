import com.horionDev.climbingapp.data.model.dto.*
import com.horionDev.climbingapp.data.model.responses.LocationInfoResponse
import com.horionDev.climbingapp.data.model.responses.StarterResponse
import com.horionDev.climbingapp.data.model.responses.SuggestionResponse
import com.horionDev.climbingapp.domain.model.*
import com.horionDev.climbingapp.domain.model.composition.Filter
import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.domain.model.composition.LocationInfos
import com.horionDev.climbingapp.domain.model.composition.AppMarker
import com.horionDev.climbingapp.domain.model.entities.Area
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Route
import com.horionDev.climbingapp.domain.model.entities.RouteGrade
import com.horionDev.climbingapp.domain.model.entities.Sector
import com.horionDev.climbingapp.domain.model.entities.User
import com.horionDev.climbingapp.utils.Constants
import com.horionDev.climbingapp.utils.FilterType
import com.horionDev.climbingapp.utils.toBool
import database.FilterEntity
import database.LocationSearchEntity
import database.SearchEntity
import kotlin.jvm.JvmName

@JvmName(name = "userDtoListToVo")
fun List<UserDto>.toVo() = map { it.toVo() }
fun UserDto.toVo() = User(
    id,
    username,
    password,
    email,
    "U",
    isSubscribe
)

@JvmName(name = "cragDtoListToVo")
fun List<CragDto>.toVo() = map { it.toVo() }
fun CragDto.toVo() = Crag(
    id,
    name,
    description,
    areaId,
    emptyList(),
    latitude,
    longitude,
    thumbnailImageUrl
)

@JvmName(name = "sectorDtoListToVo")
fun List<SectorDto>.toVo() = map { it.toVo() }
fun SectorDto.toVo() = Sector(
    id,
    name,
    description = "",
    cragId
)

@JvmName(name = "areaDtoListToVo")
fun List<AreaDto>.toVo() = map { it.toVo() }
fun AreaDto.toVo() = Area(
    id,
    name
)

@JvmName(name = "routeDtoListToVo")
fun List<RouteDto>.toVo() = map { it.toVo() }
fun RouteDto.toVo() = Route(
    id,
    name,
    description = "",
    RouteGrade.valueOf(grade),
    cragId.toString(),
    sectorId.toString()
)


//----------------------------------------------------------
//Data to domain layer
fun DealerDto.toVo() = Dealer(
    id,
    name,
    brands.split(",").dropLast(1),
    services.split(",").dropLast(1),
    address,
    postalCode,
    countryIso,
    phone,
    email,
    website,
    facebook,
    "youtube",
    "instagram",
    twitter,
    premium.toBool(),
    city,
    latitude.toDouble(),
    longitude.toDouble(),
    emptyList()
)

fun EventDto.toVo() = Event(
    id,
    name,
    descriptionFr,
    descriptionEn,
    descriptionEs,
    descriptionDe,
    descriptionNl,
    dateBegin,
    dateEnd,
    lat,
    lon,
    address,
    postalCode,
    city,
    country,
    countryIso,
    listOf(Photo(imageUrl)),
    url
)

fun PartnerDto.toVo() = Partner(
    id,
    name,
    description,
    brands.split(",").dropLast(1),
    services.split(",").dropLast(1),
    phone,
    email,
    website,
    facebook,
    "youtube",
    "instagram",
    twitter,
    premium.toBool(),
    emptyList()
)

@JvmName("toPartnerList")
fun List<PartnerDto>.toVo() = this.map { it.toVo() }

fun Event.toMarker() = AppMarker(this.id, false, this.latitude, this.longitude)

@JvmName("listEventsToMListMarker")
fun List<Event>.toMarker() = map { it.toMarker() }
fun Dealer.toMarker() = AppMarker(this.id, false, this.latitude, this.longitude)

@JvmName("listDealersToMListMarker")
fun List<Dealer>.toMarker() = map { it.toMarker() }

@JvmName("listLaundryToMListMarker")
fun LaundryDto.toMarker() = AppMarker(id!!, false, this.latitude!!.toDouble(), this.longitude!!.toDouble())
fun List<LaundryDto>.toMarker() = map { it.toMarker() }

@JvmName("listClimbingToListMarker")
fun Crag.toMarker() = AppMarker(id.toString(), false, this.latitude, this.longitude)
@JvmName("listClimbingToListMark")
fun List<Crag>.toMarker() = map { it.toMarker() }


@JvmName("toEventsVo")
fun List<EventDto>.toVo() = map { it.toVo() }


@JvmName("toPhotoVo")
fun List<DealerDto>.toVo() = map { it.toVo() }

@JvmName("toChecklistsVo")
fun List<CheckListDto>.toVo() = map { it.toVo() }
fun CheckListDto.toVo() =
    CheckList(id, name, description, imageLink, tags.split(", "), tasks.map { it.toVo() })

fun TaskDto.toVo() = Todo(id, name)


@JvmName("toAdVo")
fun List<AdDto>.toVo() = map { it.toVo() }
fun AdDto.toVo() = Ad(type, url, redirect, click)

fun StarterResponse.toVo() = Starter(
    lists.services.map { Pair(it.id, it.label) },
    lists.brands.map { Pair(it.id, it.name) },
    lists.menuLinks.toVo()
)

fun LocationInfoResponse.toVo() =
    LocationInfos(lat, lon, country, iso, address, gpsDeciTxt, gpsDmsTxt)

fun SuggestionResponse.toPlaces() = results.map { Place(it.name, Location(it.lat, it.lng)) }

fun MenuLinkDto.toVo() = MenuLink(name, subtitle, icon, url, urlstat)

@JvmName("toMenuLinkVo")
fun List<MenuLinkDto>.toVo() = map { it.toVo() }


//Domain to data layer
fun Dealer.toDto() = DealerDto(
    name = name, latitude = latitude.toString(), longitude = longitude.toString()
)

fun List<Dealer>.toDto() = map { it.toDto() }

//SQL DELIGHT
fun SearchEntity.toDto() = SearchDto(searchCategoryKey, searchLabel, timeStamp)
fun Search.toDto() = SearchDto(categoryKey, searchLabel, timeStamp)
fun SearchDto.toVo() = Search(categoryKey, searchLabel, timeStamp)

@JvmName("toSearchDtoFromEntity")
fun List<SearchEntity>.toDto() = map { it.toDto() }

@JvmName("toSearchDto")
fun List<Search>.toDto() = map { it.toDto() }

@JvmName("toSearchVo")
fun List<SearchDto>.toVo() = map { it.toVo() }


fun Search.toLocationDto() = LocationSearchDto(searchLabel, timeStamp, lat!!, lon!!)
fun LocationSearchEntity.toDto() = LocationSearchDto(label, timeStamp, lat, long)
fun LocationSearchDto.toVo() =
    Search(Constants.Persistence.SEARCH_CATEGORY_LOCATION, label, timeStamp, lat, lon)

@JvmName("toLocationSearchDtoFromEntity")
fun List<LocationSearchEntity>.toDto() = map { it.toDto() }

@JvmName("toLocationSearchDto")
fun List<Search>.toLocationDto() = map { it.toDto() }

@JvmName("toSearchVoFromLocationSearch")
fun List<LocationSearchDto>.toVo() = map { it.toVo() }

fun FilterEntity.toDto() = FilterDto(
    filterCategoryKey, filterId, isSelected
)

fun Filter.toDto() = FilterDto(
    category.name, filterId, when (isSelected) {
        false -> 0L
        true -> 1L
    }
)

fun FilterDto.toVo() = Filter(
    FilterType.valueOf(category), filterId, when (isSelected) {
        0L -> false
        1L -> true
        else -> false
    }
)

@JvmName("toFilterDto")
fun List<FilterEntity>.toDto() = map { it.toDto() }

@JvmName("toFilterVo")
fun List<FilterDto>.toVo() = map { it.toVo() }