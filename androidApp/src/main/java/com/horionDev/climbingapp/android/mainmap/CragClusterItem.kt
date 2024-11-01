package com.horionDev.climbingapp.android.mainmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.google.maps.android.compose.clustering.Clustering
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.domain.model.composition.AppMarker

data class CragClusterItem(
    val appMarker: AppMarker,
    val snippetString: String
) : ClusterItem {
    override fun getPosition() = LatLng(appMarker.latitude, appMarker.longitude)
    override fun getTitle() = appMarker.placeLinkedId
    override fun getSnippet() = snippetString
    override fun getZIndex() = 0f
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
@GoogleMapComposable
fun ClusteringMarkersMapContent(
    appMarkers: List<AppMarker>
) {
    val mountainClusterItems by remember(appMarkers) {
        mutableStateOf(
            appMarkers.map { appMarker ->
                CragClusterItem(
                    appMarker = appMarker,
                    snippetString = "test snippet"
                )
            }
        )
    }

    Clustering(
        items = appMarkers.map { appMarker ->
            CragClusterItem(
                appMarker = appMarker,
                snippetString = "test snippet"
            )
        },
        clusterItemContent = { SingleMountain() },
    )
}

@Composable
private fun SingleMountain() {
    Image(painter = painterResource(id = R.drawable.marker), contentDescription = "")
//        tint = colors.iconColor,
//        contentDescription = "",
//        modifier = Modifier
//            .size(32.dp)
//            .padding(1.dp)
//            .drawBehind {
//                drawCircle(color = colors.backgroundColor, style = Fill)
//                drawCircle(color = colors.borderColor, style = Stroke(width = 3f))
//            }
//            .padding(4.dp)
}