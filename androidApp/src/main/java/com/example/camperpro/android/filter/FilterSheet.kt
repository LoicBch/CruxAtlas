package com.example.camperpro.android.filter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.*
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.R
import com.example.camperpro.domain.model.Search
import com.example.camperpro.utils.BottomSheetOption

@Composable
fun FilterSheet() {

    var categorySelected by remember {
        mutableStateOf(FilterCategory.UNSELECTED)
    }

    var isSelectingOption by remember {
        mutableStateOf(false)
    }

    val appViewmodel = LocalDependencyContainer.current.appViewModel

    if (isSelectingOption) {
        FilterOptions(
            onItemClick = {
                categorySelected.optionSelected = it
                isSelectingOption = false
                appViewmodel.addSearch(
                    Search(
                        0,
                        categorySelected.name.lowercase(),
                        it,
                        System.currentTimeMillis()
                    )
                )
            },
            onSelectingOptionCancel = { isSelectingOption = false },
            categorySelected = categorySelected
        )
    } else {
        FilterCategorySelection(categorySelected = categorySelected,
                                onSelectButtonClick = { isSelectingOption = true },
                                onCategorySelected = {
                                    categorySelected = it
                                })
    }
}

enum class FilterCategory(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    var optionSelected: String?
) {
    GARAGE(
        R.string.filter_step1_option1,
        R.drawable.repair, null
    ),
    DEALERS(
        R.string.filter_step1_option2,
        R.drawable.dealers, null
    ),
    ACCESSORIES(
        R.string.filter_step1_option3, R.drawable.accessories, null
    ),
    UNSELECTED(R.string.filters_title, R.drawable.facebook, null)
}