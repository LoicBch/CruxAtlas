package com.horionDev.climbingapp.android.cragsDetails

import CragDetailsDto
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.horionDev.climbingapp.android.composables.Dropdown
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Sector
import toVo

@Composable
fun TopoTab(crag: Crag, cragDetailsDto: CragDetailsDto?, onSectorSelected: (Sector) -> Unit) {

    val sectors = cragDetailsDto?.sectors?.sortedBy { it.name } ?: emptyList()

    Column(Modifier.verticalScroll(rememberScrollState())) {
        sectors.forEach {
            SectorBlock(Modifier, it.toVo(), onSectorSelected)
        }
    }

//    Column {
//        var sectorSelected by remember {
//            mutableStateOf(Sector())
//        }
//
//        if (sectorSelected.name.isEmpty()) {
//            Text(
//                modifier = Modifier.padding(vertical = 18.dp),
//                text = "Sectors",
//                fontSize = 20.sp,
//                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//            )
//        } else {
//            Row(
//                modifier = Modifier.padding(vertical = 18.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    modifier = Modifier.clickable { sectorSelected = Sector() },
//                    imageVector = Icons.Sharp.ArrowBack,
//                    contentDescription = ""
//                )
//                Text(
//                    modifier = Modifier.padding(start = 4.dp),
//                    text = sectorSelected.name,
//                    fontSize = 20.sp,
//                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//                )
//            }
//        }
//
//
//        if (sectorSelected.name.isNotEmpty()) {
//            LazyColumn {
//                itemsIndexed(sectorSelected.routes) { index, route ->
//                    RouteRow(
//                        index,
//                        route
//                    ) {
//                    //                        Focus the selected route on the image
//                    }
//                }
//                item {
//                    Text(
//                        modifier = Modifier.padding(vertical = 8.dp),
//                        text = stringResource(id = R.string.routeMissing)
//                    )
//                    AppButton(
//                        isActive = true,
//                        onClick = { /*TODO*/ },
//                        modifier = Modifier,
//                        textRes = R.string.addRoute
//                    )
//                }
//            }
//        } else {
//            LazyColumn {
//
//                //                if connected ?
//
//                item {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(text = "Name")
//                        Text(text = "Grade")
//                        Text(text = "Routes")
//                    }
//                    Divider()
//                }
//
//                items(crag.sectors) { sector ->
//                    SectorRow(sector) {
//                        onSectorSelected(sector)
//                        sectorSelected = sector
//                    }
//                }
//
//                item {
//                    Text(
//                        modifier = Modifier.padding(vertical = 8.dp),
//                        text = stringResource(id = R.string.sectorMissing)
//                    )
//                    AppButton(
//                        isActive = true,
//                        onClick = { /*TODO*/ },
//                        modifier = Modifier,
//                        textRes = R.string.addSector
//                    )
//                }
//            }
//            Divider()
//        }
//    }
}

@Composable
fun SectorBlock(modifier: Modifier = Modifier, sector: Sector, onSectorClick: (Sector) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .clickable {
                onSectorClick(sector)
            }
    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(AppColor.PlaceHolderImage)
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(text = "Sector ${sector.name} ")
//            Text(text = sector.routes.size.toString() + " routes")
//        }

        Dropdown(Modifier, "Sector ${sector.name} " + sector.routes.distinctBy { it.name }.size.toString() + " routes") {
            sector.routes.distinctBy { it.name }.forEach {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(1.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var expanded by remember { mutableStateOf(false) }

                    Text(text = it.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = it.grade)
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.MoreVert,
                            contentDescription = ""
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            } // Fermer le menu lorsque l'utilisateur clique en dehors
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                }
                            ) {
                                Text("Share")
                            }
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                }
                            ) {
                                Text("See on 3d")
                            }
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                }
                            ) {
                                Text("More")
                            }
                        }
                    }
                }
            }


        }
    }
//    Divider()
}


//@Composable
//fun SectorRow(sector: Sector, onSectorClick: (Sector) -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 18.dp)
//            .clickable {
//                onSectorClick(sector)
//            },
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(modifier = Modifier.weight(4.5f), text = sector.name)
////        Text(modifier = Modifier.weight(2f), text = "${RouteGrade.values()[sector.routes.minOf {
////            it.grade.ordinal }].displayValue} to ${RouteGrade.values()[sector.routes.maxOf { it.grade.ordinal }].displayValue}")
////        Text(modifier = Modifier.weight(2.5f), text = sector.routes.size.toString(), textAlign =
////        TextAlign.End)
//    }
//    Divider()
//}

//@Composable
//fun RouteRow(index: Int, routeItem: Route, onRouteClick: (Route) -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 18.dp)
//            .clickable {
//                onRouteClick(routeItem)
//            },
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Absolute.SpaceBetween
//    ) {
//        Text(
//            modifier = Modifier
//                .background(
//                    color = Color.Gray, shape = RoundedCornerShape(15)
//                )
//                .padding(16.dp),
//            text = (index + 1).toString(),
//            color = Color.White,
//            fontSize = 18.sp,
//            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//        )
//        Text(text = routeItem.name)
////        Text(text = routeItem.grade.displayValue)
//    }
//    Divider()
//
//}