import com.jetbrains.kmm.shared.data.model.dto.SpotDto
import com.jetbrains.kmm.shared.domain.model.Spot

//Data to domain layer
fun SpotDto.toVo(): Spot = Spot(this.titre, this.latitude.toDouble(), this.longitude.toDouble())
fun List<SpotDto>.toVo(): List<Spot> = this.map { it.toVo() }

//Domain to data layer
fun Spot.toDto(): SpotDto = SpotDto(name = this.name, latitude = this.lat.toString(), longitude = this.long.toString())
fun List<Spot>.toDto() : List<SpotDto> = this.map { it.toDto() }

