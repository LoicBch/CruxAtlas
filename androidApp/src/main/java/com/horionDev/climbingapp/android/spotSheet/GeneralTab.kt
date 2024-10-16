package com.horionDev.climbingapp.android.spotSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.ChartDataCollection
import com.himanshoe.charty.common.config.AxisConfig
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.composables.SmallAppButton
import com.horionDev.climbingapp.android.destinations.AddSpotInformationsScreenDestination
import com.horionDev.climbingapp.android.destinations.AddSpotLocationScreenDestination
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Route
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun GeneralTab(crag: Crag, navigator: DestinationsNavigator) {
    val scrollState = rememberScrollState()
    val cragRoutes = crag.sectors.flatMap { it.routes }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxHeight()
    ) {
        ActionRow()
        Divider()
        CragInfos(crag, navigator = navigator)
        Divider()
        RoutesChart(cragRoutes)
        Divider()
        SeasonInfos()
    }
}

@Composable
fun RoutesChart(routes: List<Route>) {
    val chartData = mutableListOf<BarData>()
    routes
        .groupBy(Route::grade)
        .toSortedMap(compareBy { it })
        .forEach {
            chartData.add(
                BarData(
                    it.value.size.toFloat(),
                    it.key,
                    color = AppColor.Primary,
                )
            )
        }

    Text(
        modifier = Modifier.padding(vertical = 16.dp),
        text = "Routes",
        fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
        fontWeight = FontWeight.W500,
        color = Color.Black
    )

    BarChart(
        modifier = Modifier
            .height(150.dp),
        dataCollection = ChartDataCollection(chartData.dropLast(1)),
        axisConfig = AxisConfig(true, true, true, 2f, 14, Color.Black)
    )
}

@Composable
fun CragInfos(crag: Crag, navigator: DestinationsNavigator) {

    Row(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = "Informations",
            fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight.W500,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { navigator.navigate(AddSpotInformationsScreenDestination(crag)) }) {
            Icon(imageVector = Icons.Sharp.Add, contentDescription = "")
        }
    }

    if (crag.altitude == null && crag.orientation == null && crag.approachLenght == null) {
        MissingDataBlock(R.string.no_infos, onAddDataClick = { /*TODO*/ })
    } else {
        MissingDataBlock(
            R.string.no_infos,
            onAddDataClick = {
                navigator.navigate(
                    AddSpotInformationsScreenDestination(crag)
                )
            })
        //        val infos = listOf(
        //            Triple(R.drawable.accessories, "Orienration :", crag.orientation),
        //            Triple(R.drawable.accessories, "Altitude :", crag.altitude),
        //            Triple(R.drawable.accessories, "Approach lenght :", crag.approachLenght),
        //            Triple(R.drawable.accessories, "others :", "others")
        //        )
        //
        //        infos.forEach {
        //            Row(
        //                Modifier
        //                    .fillMaxWidth()
        //                    .padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically
        //            ) {
        //                Icon(
        //                    modifier = Modifier.padding(end = 6.dp),
        //                    painter = painterResource(id = it.first),
        //                    contentDescription = ""
        //                )
        //                Text(modifier = Modifier.padding(end = 4.dp), text = it.second)
        //                Text(text = it.third!!)
        //            }
        //        }
    }

}

@Composable
fun ActionRow() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionButton(title = R.string.favorite, icon = R.drawable.love)
        ActionButton(title = R.string.view_in_3D, icon = R.drawable.modeling)
        ActionButton(title = R.string.navigate, icon = R.drawable.direction)
    }
}

@Composable
fun ActionButton(title: Int, icon: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(25),
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(25)
                ), onClick = {

            }) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.cd_button_vertical_list)
            )
        }
        Text(text = stringResource(id = title))
    }
}

@Composable
fun SeasonInfos() {

    Row(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = "Best seasons",
            fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight.W500,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Sharp.Add, contentDescription = "")
        }
    }

    if (false) {
        MissingDataBlock(R.string.no_seasons, onAddDataClick = { /*TODO*/ })
    } else {
        val months =
            listOf(
                "Jan" to false,
                "Feb" to false,
                "Mar" to false,
                "Apr" to false,
                "May" to true,
                "Jun" to true,
                "Jul" to true,
                "Aug" to true,
                "Sep" to true,
                "Oct" to false,
                "Nov" to false,
                "Dec" to false
            )

        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
            months.forEach {
                Text(
                    modifier = Modifier.background(
                        color = if (it.second) AppColor.BlueCamperPro else Color.Gray,
                        shape = RoundedCornerShape(0)
                    ), text = it.first
                )
            }
        }
    }
}

@Composable
fun MissingDataBlock(text: Int, onAddDataClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(id = text),
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        SmallAppButton(
            isActive = true,
            onClick = { onAddDataClick() },
            modifier = Modifier.padding(horizontal = 20.dp),
            textRes = R.string.help_add_data
        )
    }
}