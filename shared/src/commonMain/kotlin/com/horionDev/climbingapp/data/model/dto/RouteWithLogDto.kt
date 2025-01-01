import com.horionDev.climbingapp.data.model.dto.RouteDto
import com.horionDev.climbingapp.data.model.dto.RouteLogDto
import kotlinx.serialization.Serializable

@Serializable
class RouteWithLogDto(
    var log: RouteLogDto,
    var route: RouteDto
)