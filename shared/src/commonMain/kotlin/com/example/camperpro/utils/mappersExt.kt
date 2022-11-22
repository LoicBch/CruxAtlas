import com.example.camperpro.data.model.dto.SpotDto
import com.example.camperpro.domain.model.Photo
import com.jetbrains.kmm.shared.data.model.dto.PhotoDto
import com.example.camperpro.domain.model.Spot

//Data to domain layer
fun SpotDto.toVo() = Spot(this.titre, this.descriptionFr!!, this.utilisateurCreation!!, this.latitude.toDouble(), this.longitude.toDouble(), this.photos.toVo())
fun List<SpotDto>.toVo() = this.map { it.toVo() }

fun PhotoDto.toVo() = Photo(this.linkLarge!!)
fun ArrayList<PhotoDto>.toVo() : List<Photo> = this.map { Photo(it.linkLarge!!) }


//Domain to data layer
fun Spot.toDto() = SpotDto(name = this.name, latitude = this.lat.toString(), longitude = this.long.toString())
fun List<Spot>.toDto()  = this.map { it.toDto() }

