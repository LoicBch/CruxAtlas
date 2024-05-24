package com.example.camperpro.android.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.R
import com.example.camperpro.android.filter.LastSearchItem
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.domain.model.composition.filterName
import com.example.camperpro.utils.FilterType

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
