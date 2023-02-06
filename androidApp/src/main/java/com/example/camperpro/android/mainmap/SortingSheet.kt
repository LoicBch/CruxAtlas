package com.example.camperpro.android.mainmap

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.AppButton
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.utils.BottomSheetOption
import com.example.camperpro.utils.SortOption
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SortingSheet(spotsSource: BottomSheetOption) {

    var categorySelected by remember {
        mutableStateOf(SortCategory.NONE)
    }

    val coroutine = rememberCoroutineScope()
    val appViewModel = LocalDependencyContainer.current.appViewModel
    val sheetState = appViewModel.bottomSheetIsShowing

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { coroutine.launch { sheetState.hide() } }) {
                Icon(
                    imageVector = Icons.Sharp.Close,
                    contentDescription = "",
                    tint = AppColor.Tertiary
                )
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                text = stringResource(
                    id = R.string.sorting
                ), color = Color.Black
            )
            Spacer(modifier = Modifier.weight(0.5f))
            if (categorySelected != SortCategory.NONE) {
                IconButton(onClick = { categorySelected = SortCategory.NONE }) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "",
                        tint = AppColor.Secondary
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(top = 63.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.W700,
            text = stringResource(
                id = R.string.sort_places
            )
        )

        Divider(modifier = Modifier.padding(top = 13.dp))

        SortCategory.values().availableSorting(spotsSource).forEach { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .selectable(
                        selected = (categorySelected == category),
                        onClick = { categorySelected = category },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SortRadioGroupItem(
                    sortCategory = category,
                    sortCategorySelected = categorySelected
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            modifier = Modifier.padding(bottom = 25.dp),
            isActive = categorySelected != SortCategory.NONE,
            onClick = {
                appViewModel.onSortingOptionSelected(categorySelected.toSortOption())
                coroutine.launch { sheetState.hide() }
            },
            textRes = R.string.apply_sorting
        )
    }
}

@Composable
fun RowScope.SortRadioGroupItem(sortCategory: SortCategory, sortCategorySelected: SortCategory) {
    Icon(
        painter = painterResource(id = sortCategory.icon),
        contentDescription = "",
        tint = if (sortCategorySelected == sortCategory) AppColor.Primary else AppColor.Tertiary
    )
    Text(
        modifier = Modifier.padding(start = 10.dp),
        text = stringResource(sortCategory.title),
        fontSize = 14.sp,
        fontWeight = FontWeight.W500,
        color = if (sortCategorySelected == sortCategory) AppColor.Primary else AppColor.Tertiary
    )
    Spacer(modifier = Modifier.weight(0.5f))
    RadioButton(
        selected = (sortCategorySelected == sortCategory),
        onClick = null,
        colors = RadioButtonDefaults.colors(selectedColor = if (sortCategorySelected == sortCategory) AppColor.Primary else AppColor.Tertiary)
    )
}

enum class SortCategory(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    NONE(
        R.string.none,
        R.drawable.circle_cross
    ),
    DIST_FROM_YOU(
        R.string.by_distance_from_you,
        R.drawable.distance
    ),
    DIST_FROM_SEARCHED(
        R.string.distance_from_searched_location, R.drawable.distance
    ),
    BY_DATE(
        R.string.by_date, R.drawable.events
    )
}

// TODO: clean ca
fun SortCategory.toSortOption(): SortOption {
    return when (this) {
        SortCategory.NONE -> SortOption.NONE
        SortCategory.DIST_FROM_YOU -> SortOption.DIST_FROM_YOU
        SortCategory.DIST_FROM_SEARCHED -> SortOption.DIST_FROM_SEARCHED
        SortCategory.BY_DATE -> SortOption.BY_DATE
    }
}

fun Array<SortCategory>.availableSorting(spotsSource: BottomSheetOption): List<SortCategory> {
    return when (spotsSource) {
        BottomSheetOption.SORT -> listOf(SortCategory.NONE, SortCategory.DIST_FROM_YOU)
        BottomSheetOption.SORT_AROUND_PLACE -> listOf(
            SortCategory.NONE,
            SortCategory.DIST_FROM_YOU,
            SortCategory.DIST_FROM_SEARCHED
        )
        BottomSheetOption.SORT_EVENTS -> listOf(
            SortCategory.NONE,
            SortCategory.DIST_FROM_YOU,
            SortCategory.BY_DATE
        )
        else -> {
            this.toList()
        }
    }
}
