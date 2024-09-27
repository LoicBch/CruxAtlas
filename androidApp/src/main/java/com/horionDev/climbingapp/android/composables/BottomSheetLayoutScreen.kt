package com.horionDev.climbingapp.android.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.LocalDependencyContainer
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.domain.model.entities.RouteGrade
import com.horionDev.climbingapp.utils.Globals
import com.horionDev.climbingapp.utils.Globals.filters.countries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class FilterOptions {
    DIFFICULTY, OtherClimbingFilters
}

@OptIn(ExperimentalLayoutApi::class)
@ExperimentalMaterialApi
@Composable
fun BottomSheetLayoutScreen() {

    val grades = RouteGrade.values()
    val initialState = grades.map { false }
    val checkedStateGrades = remember { mutableStateListOf(*initialState.toTypedArray()) }


    val countries = Globals.filters.countries
    val initialStateCountries = countries.map { false }
    val checkedStateCountries = remember { mutableStateListOf(*initialState.toTypedArray()) }


    val sheetState = LocalDependencyContainer.current.appViewModel.bottomSheetIsShowing
    val appViewModel = LocalDependencyContainer.current.appViewModel
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        FilterHeader(scope, sheetState, checkedStateGrades, checkedStateCountries)
        GradesFilters(grades, checkedStateGrades)
        CountriesFilters(countries, checkedStateCountries)
        AppButton(
            isActive = true,
            onClick = {
                //                    appViewModel.applyFilter(checkedState.mapIndexed { a, b -> if (b) types[a] else null }
                //                                                 .filterNotNull())
                scope.launch {
                    sheetState.hide()
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 10.dp),
            textRes = R.string.apply_filters
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CountriesFilters(
    countries: List<String>,
    checkedStateCountries: SnapshotStateList<Boolean>
) {

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {

        Text(
            text = "By countries",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.oppinsedium))
        )
        Divider()

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            countries.forEachIndexed { index, country ->
                FilterChip(
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    isChecked = checkedStateCountries[index],
                    onClick = { checkedStateCountries[index] = !checkedStateCountries[index] },
                    label = countries[index]
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun GradesFilters(
    grades: Array<RouteGrade>,
    checkedState: SnapshotStateList<Boolean>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "By grades",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.oppinsedium))
        )
        Divider()

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            grades.forEachIndexed { index, grade ->
                FilterChip(
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    isChecked = checkedState[index],
                    onClick = { checkedState[index] = !checkedState[index] },
                    label = grades[index].displayValue
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FilterHeader(
    scope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    checkedGrades: SnapshotStateList<Boolean>, checkedCountries: SnapshotStateList<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            scope.launch {
                sheetState.hide()
            }
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Filters",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            fontFamily = FontFamily(
                Font(R.font.oppinsedium)
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Reset all ${checkedGrades.filter { it }.size + checkedCountries.filter { it }.size}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = FontFamily(
                Font(R.font.oppinsedium)
            ),
            color = AppColor.white,
            modifier = Modifier
                .padding(end = 10.dp)
                .alpha(if (checkedGrades.any { it } || checkedCountries.any { it }) 1f else 0f)
                .background(Color.Black, shape = RoundedCornerShape(5.dp))
                .padding(6.dp)
                .clickable { checkedGrades.replaceAll { false }; checkedCountries.replaceAll { false } }
        )
    }
}


@Composable
fun FilterChip(
    modifier: Modifier,
    isChecked: Boolean,
    onClick: () -> Unit,
    label: String,
    leadingIcon: Int? = null
) {
    Row(
        modifier
            .defaultMinSize(minWidth = 60.dp)
            .padding(horizontal = 2.dp)
            .background(
                if (isChecked) AppColor.Black else Color.Unspecified,
                shape = RoundedCornerShape(5.dp)
            )
            .border(
                width = 1.dp,
                color = if (isChecked) Color.Unspecified else Color.Black,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(vertical = 5.dp)
            .padding(horizontal = 10.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        leadingIcon?.let {
            Image(
                painter = painterResource(id = leadingIcon),
                contentDescription = "",
            )
        }

        Text(
            text = label,
            color = if (isChecked) Color.White else Color.Black,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.oppinsedium))
        )

//        if (isChecked) {
//            Icon(
//                modifier = Modifier.padding(start = 5.dp),
//                imageVector = Icons.Filled.Done,
//                contentDescription = "",
//                tint = Color.Green
//            )
//        }
    }
}
