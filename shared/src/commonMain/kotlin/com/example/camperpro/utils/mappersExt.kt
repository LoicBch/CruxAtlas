import com.example.camperpro.data.model.dto.AdDto
import com.example.camperpro.data.model.dto.MenuLinkDto
import com.example.camperpro.data.model.dto.SpotDto
import com.example.camperpro.data.model.responses.StarterResponse
import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.MenuLink
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.domain.model.Starter
import kotlin.jvm.JvmName

//Data to domain layer
fun SpotDto.toVo() =
    Spot(name = this.name, city = this.city!!, this.latitude.toDouble(), this.longitude.toDouble())

fun List<SpotDto>.toVo() = this.map { it.toVo() }

fun AdDto.toVo() = Ad(this.type, this.url, this.click)

@JvmName("toAdVo")
fun List<AdDto>.toVo() = this.map { it.toVo() }

fun StarterResponse.toVo() = Starter(
    this.lists.services.map { it.label },
    this.lists.brands.map { it.name },
    this.lists.menuLinks.toVo()
)

fun MenuLinkDto.toVo() = MenuLink(this.name, this.subtitle, this.icon, this.url, this.urlstat)

@JvmName("toMenuLinkVo")
fun List<MenuLinkDto>.toVo() = this.map { it.toVo() }

//Domain to data layer
fun Spot.toDto() = SpotDto(
    name = this.name, latitude = this.latitude.toString(), longitude = this.longitude.toString()
)

fun List<Spot>.toDto() = this.map { it.toDto() }




