import com.example.camperpro.data.model.dto.SpotDto
import com.example.camperpro.domain.model.Photo
import com.jetbrains.kmm.shared.data.model.dto.PhotoDto
import com.example.camperpro.domain.model.Spot
import kotlinx.coroutines.*

//Data to domain layer
fun SpotDto.toVo() = Spot(name = this.name, city = this.city!!, this.latitude.toDouble(), this.longitude.toDouble())
fun List<SpotDto>.toVo() = this.map { it.toVo() }

//Domain to data layer
fun Spot.toDto() = SpotDto(name = this.name, latitude = this.latitude.toString(), longitude = this.longitude.toString())
fun List<Spot>.toDto() = this.map { it.toDto() }



