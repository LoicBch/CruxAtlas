package com.appmobiledition.laundryfinder.android.filter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import com.appmobiledition.laundryfinder.android.LocalDependencyContainer
import com.appmobiledition.laundryfinder.android.R
import com.appmobiledition.laundryfinder.utils.FilterType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterSheet() {

    val appViewmodel = LocalDependencyContainer.current.appViewModel
    val coroutine = rememberCoroutineScope()
    val sheetState = appViewmodel.bottomSheetIsShowing
    val filterSelected by appViewmodel.filterDealerSelected.collectAsState()

    var isSelectingOption by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        appViewmodel.getFilter()
        isSelectingOption = false
    }

    if (!appViewmodel.bottomSheetIsShowing.isVisible) isSelectingOption = false

    if (isSelectingOption) {
        FilterOptions(
            onItemClick = {
                isSelectingOption = false
                appViewmodel.onFilterOptionSelected(it)
            },
            onSelectingOptionCancel = { isSelectingOption = false },
            categorySelected = filterSelected.category
        )
    } else {
        FilterCategorySelection(filterSelected = filterSelected,
                                onSelectButtonClick = { isSelectingOption = true },
                                onCategorySelected = {
                                    appViewmodel.onFilterCategorySelected(it)
                                },
                                onApplyFilter = {
                                    appViewmodel.applyFilterToDealers()
                                    coroutine.launch { sheetState.hide() }
                                },
                                onHistoricFilterSelection = { appViewmodel.onFilterOptionSelected(it) }
        )
    }
}

enum class RadioButtonsFilter(
    @StringRes val title: Int, @DrawableRes val icon: Int, val filterType: FilterType
) {
    GARAGE(
        R.string.filter_step1_option1, R.drawable.repair, FilterType.SERVICE
    ),
    DEALERS(
        R.string.filter_step1_option2, R.drawable.dealers, FilterType.BRAND
    ),
    UNSELECTED(R.string.filters_title, R.drawable.facebook, FilterType.UNSELECTED_DEALER)
}

fun RadioButtonsFilter.toDomainEnum() = when (this) {
    RadioButtonsFilter.GARAGE -> FilterType.SERVICE
    RadioButtonsFilter.DEALERS -> FilterType.BRAND
    RadioButtonsFilter.UNSELECTED -> RadioButtonsFilter.UNSELECTED
}