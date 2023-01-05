import com.example.camperpro.data.model.dto.*
import com.example.camperpro.data.model.responses.StarterResponse
import com.example.camperpro.data.model.responses.SuggestionResponse
import com.example.camperpro.domain.model.*
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.toBool
import database.LocationSearchEntity
import database.SearchEntity
import kotlin.jvm.JvmName

//Data to domain layer
fun SpotDto.toVo() =
    Spot(
        id,
        name,
        distance,
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

fun PhotoDto.toVo() = Photo(url)

@JvmName("toPhotoVo")
fun List<PhotoDto>.toVo() = map { it.toVo() }
fun List<SpotDto>.toVo() = map { it.toVo() }
fun AdDto.toVo() = Ad(type, url, click)

@JvmName("toAdVo")
fun List<AdDto>.toVo() = map { it.toVo() }

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
fun Spot.toDto() = SpotDto(
    name = name, latitude = latitude.toString(), longitude = longitude.toString()
)

fun List<Spot>.toDto() = map { it.toDto() }


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


fun LocationSearchEntity.toDto() = LocationSearchDto(id, label, timeStamp, lat.toDouble(), long.toDouble())
fun Search.toLocationDto() = LocationSearchDto(id, searchLabel, timeStamp, lat!!, lon!!)
fun LocationSearchDto.toVo() =
    Search(id, Constants.Persistence.SEARCH_CATEGORY_LOCATION, label, timeStamp, lat, lon)

@JvmName("toLocationSearchDtoFromEntity")
fun List<LocationSearchEntity>.toDto() = map { it.toDto() }

@JvmName("toLocationSearchDto")
fun List<Search>.toLocationDto() = map { it.toDto() }

@JvmName("toSearchVoFromLocationSearch")
fun List<LocationSearchDto>.toVo() = map { it.toVo() }