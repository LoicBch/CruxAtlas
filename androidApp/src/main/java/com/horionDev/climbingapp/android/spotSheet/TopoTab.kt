package com.horionDev.climbingapp.android.spotSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.domain.model.composition.BoundingBox
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Route
import com.horionDev.climbingapp.domain.model.entities.RouteGrade
import com.horionDev.climbingapp.domain.model.entities.Sector
import kotlin.reflect.KProperty

@Composable
fun TopoTab(crag: Crag, onSectorSelected: (Sector) -> Unit) {
    Column {

        var sectorSelected by remember {
            mutableStateOf(Sector())
        }

        if (sectorSelected.name.isEmpty()) {
            Text(
                modifier = Modifier.padding(vertical = 18.dp),
                text = "Sectors",
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        } else {
            Row(
                modifier = Modifier.padding(vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.clickable { sectorSelected = Sector() },
                    imageVector = Icons.Sharp.ArrowBack,
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = sectorSelected.name,
                    fontSize = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }


        if (sectorSelected.name.isNotEmpty()) {
            LazyColumn {
                itemsIndexed(sectorSelected.routes) { index, route ->
                    RouteRow(
                        index,
                        route
                    ) {
                    //                        Focus the selected route on the image
                    }
                }
                item {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = stringResource(id = R.string.routeMissing)
                    )
                    AppButton(
                        isActive = true,
                        onClick = { /*TODO*/ },
                        modifier = Modifier,
                        textRes = R.string.addRoute
                    )
                }
            }
        } else {
            LazyColumn {

                //                if connected ?

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Name")
                        Text(text = "Grade")
                        Text(text = "Routes")
                    }
                    Divider()
                }

                items(crag.sectors) { sector ->
                    SectorRow(sector) {
                        onSectorSelected(sector)
                        sectorSelected = sector
                    }
                }

                item {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = stringResource(id = R.string.sectorMissing)
                    )
                    AppButton(
                        isActive = true,
                        onClick = { /*TODO*/ },
                        modifier = Modifier,
                        textRes = R.string.addSector
                    )
                }
            }
            Divider()
        }
    }
}


@Composable
fun SectorRow(sector: Sector, onSectorClick: (Sector) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .clickable {
                onSectorClick(sector)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(modifier = Modifier.weight(4.5f), text = sector.name)
//        Text(modifier = Modifier.weight(2f), text = "${RouteGrade.values()[sector.routes.minOf {
//            it.grade.ordinal }].displayValue} to ${RouteGrade.values()[sector.routes.maxOf { it.grade.ordinal }].displayValue}")
//        Text(modifier = Modifier.weight(2.5f), text = sector.routes.size.toString(), textAlign =
//        TextAlign.End)
    }
    Divider()
}

@Composable
fun RouteRow(index: Int, routeItem: Route, onRouteClick: (Route) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .clickable {
                onRouteClick(routeItem)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .background(
                    color = Color.Gray, shape = RoundedCornerShape(15)
                )
                .padding(16.dp),
            text = (index + 1).toString(),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Text(text = routeItem.name)
//        Text(text = routeItem.grade.displayValue)
    }
    Divider()

}