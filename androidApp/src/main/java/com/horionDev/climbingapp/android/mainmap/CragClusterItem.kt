package com.horionDev.climbingapp.android.mainmap

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.domain.model.composition.AppMarker

enum class ClusteringType {
    CRAG,
    BOULDER,
    ROUTE
}

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
    appMarkers: List<AppMarker>,
    screenWidth: Int,
    screenHeight: Int
) {

    val nonHierarchicalViewBasedAlgorithm = NonHierarchicalViewBasedAlgorithm<CragClusterItem>(
        screenWidth,
        screenHeight
    )

    val distanceBasedAlgorithm = NonHierarchicalDistanceBasedAlgorithm<CragClusterItem>().apply {
        maxDistanceBetweenClusteredItems = 100
    }

    val clusterManager = rememberClusterManager<CragClusterItem>()

    clusterManager?.let { manager ->
        val renderer = rememberClusterRenderer(clusterContent = {
            IconAsClusterContent(it)
        }, clusterItemContent = {
            IconAsClusterContentItem(it)
        }, clusterManager = manager)

        clusterManager.algorithm = nonHierarchicalViewBasedAlgorithm

        SideEffect {
            if (manager.renderer != renderer) {
                manager.renderer = renderer ?: return@SideEffect
            }
        }

        Clustering(
            items = appMarkers.map { appMarker ->
                CragClusterItem(
                    appMarker = appMarker,
                    snippetString = appMarker.placeLinkedId
                )
            },
            clusterManager = manager,
//        clusterItemContent = { SingleMountain() },
        )
    }
}

@Composable
private fun IconAsClusterContentItem(data: CragClusterItem) {
    Image(painter = painterResource(id = R.drawable.marker), contentDescription = "")
}

@Composable
private fun IconAsClusterContent(cluster: Cluster<CragClusterItem>) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.Blue, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = cluster.size.toString(),
            color = Color.White,
            style = MaterialTheme.typography.body2
        )
    }
}

//@Composable
//private fun SingleMountain() {
//    Image(painter = painterResource(id = R.drawable.marker), contentDescription = "")
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
//}