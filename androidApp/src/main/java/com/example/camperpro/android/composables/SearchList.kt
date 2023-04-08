package com.example.camperpro.android.composables

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    val scrollState = rememberScrollState()

    Text(
        modifier = Modifier.padding(top = 20.dp),
        text = stringResource(id = R.string.last_searched),
        color = AppColor.outlineText,
        fontSize = 14.sp,
        fontWeight = FontWeight.W500
    )

    LazyColumn(
        modifier = Modifier
            .padding(top = 22.dp)
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(filters.value.filter { it.category == categorySelected }.take(2)) { filter ->
            LastSearchItem(onSearchDelete = {
                appViewmodel.removeFilter(filter)
            }, onSelectSearch = {
                onSelectFilter(it)
            }, search = filter.filterName)
        }
    }
}
