import com.example.camperpro.data.model.dto.*
import com.example.camperpro.data.model.responses.StarterResponse
import com.example.camperpro.domain.model.*
import com.example.camperpro.utils.toBool
import database.SearchEntity
import kotlin.jvm.JvmName

//Data to domain layer
fun SpotDto.toVo() =
    Spot(
        id,
        name,
        distance,
        brands.split(","),
        services.split(","),
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


