package com.horionDev.climbingapp.android.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.horionDev.climbingapp.android.LocalDependencyContainer
import com.horionDev.climbingapp.android.filter.LastSearchItem
import com.horionDev.climbingapp.domain.model.composition.filterName
import com.horionDev.climbingapp.utils.FilterType

@Composable
fun HistoricSearchList(categorySelected: FilterType, onSelectFilter: (String) -> Unit) {

    val appViewmodel = LocalDependencyContainer.current.appViewModel
    val filters by appViewmodel.filtersDealerUsed.collectAsStateWithLifecycleImmutable()

    filters.value.filter { it.category == categorySelected }.take(2).distinct().forEach { filter ->
        LastSearchItem(onSearchDelete = {
            appViewmodel.removeFilter(filter)
        }, onSelectSearch = {
            onSelectFilter(it)
        }, search = filter.filterName)
    }
}
