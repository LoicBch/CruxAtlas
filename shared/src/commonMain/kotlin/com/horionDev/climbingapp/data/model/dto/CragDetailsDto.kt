import com.horionDev.climbingapp.data.model.dto.ModelDto
import com.horionDev.climbingapp.data.model.dto.SectorDto
import com.horionDev.climbingapp.data.repositories.Models
import com.horionDev.climbingapp.domain.model.entities.Model
import kotlinx.serialization.Serializable

@Serializable
class CragDetailsDto(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val thumbnailUrl: String,
    val sectors: List<SectorDto>,
    val models: List<ModelDto>
)