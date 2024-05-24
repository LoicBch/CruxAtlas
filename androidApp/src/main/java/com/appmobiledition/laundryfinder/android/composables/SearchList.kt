package com.appmobiledition.laundryfinder.android.composables

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
import com.appmobiledition.laundryfinder.android.LocalDependencyContainer
import com.appmobiledition.laundryfinder.android.R
import com.appmobiledition.laundryfinder.android.filter.LastSearchItem
import com.appmobiledition.laundryfinder.android.ui.theme.AppColor
import com.appmobiledition.laundryfinder.domain.model.composition.filterName
import com.appmobiledition.laundryfinder.utils.FilterType

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
