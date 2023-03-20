import com.example.camperpro.data.model.dto.*
import com.example.camperpro.data.model.responses.StarterResponse
import com.example.camperpro.data.model.responses.SuggestionResponse
import com.example.camperpro.domain.model.*
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.Marker
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.toBool
import database.LocationSearchEntity
import database.SearchEntity
import kotlin.jvm.JvmName

//Data to domain layer
fun DealerDto.toVo() =
    Dealer(
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
        photos.toVo()
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
    photos.toVo()
)

@JvmName("toPartnerList")
fun List<PartnerDto>.toVo() = this.map { it.toVo() }

fun Event.toMarker() = Marker(this.id, false, this.latitude, this.longitude)

@JvmName("listEventsToMListMarker")
fun List<Event>.toMarker() = map { it.toMarker() }
fun Dealer.toMarker() = Marker(this.id, false, this.latitude, this.longitude)

@JvmName("listDealersToMListMarker")
fun List<Dealer>.toMarker() = map { it.toMarker() }

@JvmName("toEventsVo")
fun List<EventDto>.toVo() = map { it.toVo() }

fun PhotoDto.toVo() = Photo(url)

@JvmName("toPhotoVo")
fun List<PhotoDto>.toVo() = map { it.toVo() }
fun List<DealerDto>.toVo() = map { it.toVo() }


@JvmName("toAdVo")
fun List<AdDto>.toVo() = map { it.toVo() }
fun AdDto.toVo() = Ad(type, url, redirect, click)

fun StarterResponse.toVo() = Starter(
    lists.services.map { Pair(it.id, it.label) },
    lists.brands.map { Pair(it.id, it.name) },
    lists.menuLinks.toVo()
)

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
fun SearchEntity.toDto() = SearchDto(id, searchCategoryKey, searchLabel, timeStamp)
fun Search.toDto() = SearchDto(id, categoryKey, searchLabel, timeStamp)
fun SearchDto.toVo() = Search(id, categoryKey, searchLabel, timeStamp)

@JvmName("toSearchDtoFromEntity")
fun List<SearchEntity>.toDto() = map { it.toDto() }

@JvmName("toSearchDto")
fun List<Search>.toDto() = map { it.toDto() }

@JvmName("toSearchVo")
fun List<SearchDto>.toVo() = map { it.toVo() }


fun LocationSearchEntity.toDto() =
    LocationSearchDto(id, label, timeStamp, lat, long.toDouble())

fun Search.toLocationDto() = LocationSearchDto(id, searchLabel, timeStamp, lat!!, lon!!)
fun LocationSearchDto.toVo() =
    Search(id, Constants.Persistence.SEARCH_CATEGORY_LOCATION, label, timeStamp, lat, lon)

@JvmName("toLocationSearchDtoFromEntity")
fun List<LocationSearchEntity>.toDto() = map { it.toDto() }

@JvmName("toLocationSearchDto")
fun List<Search>.toLocationDto() = map { it.toDto() }

@JvmName("toSearchVoFromLocationSearch")
fun List<LocationSearchDto>.toVo() = map { it.toVo() }