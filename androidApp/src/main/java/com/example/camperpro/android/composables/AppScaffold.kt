package com.example.camperpro.android.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.camperpro.android.*
import com.example.camperpro.android.R
import com.example.camperpro.android.destinations.AndroidAppDestination
import com.example.camperpro.android.ui.theme.AppColor
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.spec.Route

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AppScaffold(
    startRoute: Route,
    navController: NavController,
    topBar: Boolean,
    sheetState: ModalBottomSheetState,
    bottomBar: @Composable (AndroidAppDestination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val destination =
        navController.appCurrentDestinationAsState().value ?: startRoute.startAppDestination

    ModalBottomSheetLayout(
        sheetState = sheetState, sheetContent = {
            FilterScreen()
        }, scrimColor = Color.Black.copy(alpha = 0.32f)
    ) {
        Scaffold(topBar = { }, bottomBar = {
            if (destination.shouldShowBottomBar) bottomBar(destination)
        }, content = content
        )
    }
}

val AndroidAppDestination.shouldShowBottomBar get() = AndroidConstants.ScreensAboveBottomBar.any { this.route == it.route }

@Composable
fun FilterScreen() {

    val appViewmodel = LocalDependencyContainer.current.appViewModel
    val context = LocalContext.current
    val filters by appViewmodel.filters.collectAsState()
    val lastSearches by appViewmodel.lastFilterSearch.collectAsState()
    val filterKeyCurrentlyOpen = remember {
        mutableStateOf("")
    }


    Column(modifier = Modifier.padding(vertical = 27.dp, horizontal = 16.dp)) {
        if (filterKeyCurrentlyOpen.value.isEmpty()) {
            FilterCategory(filters, updateBooleanPref = { key, value ->
                appViewmodel.updateBooleanPreference(
                    context, key, value
                )
            })
        } else {
            FilterSearch(filterKeyCurrentlyOpen.value, lastSearches, emptyList())
        }
    }
}

@Composable
fun FilterSearch(filterLabel: String, lastSearches: Set<String>, options: List<String>) {

    var atLeastOneFilterHasChanged by remember {
        mutableStateOf(false)
    }

    Text(
        text = filterLabel, fontWeight = FontWeight.Black, fontSize = 16.sp
    )

    SearchField(Modifier, R.string.search_placeholder)
    Text(
        text = stringResource(id = R.string.last_searched),
        fontSize = 10.sp,
        fontWeight = FontWeight.Black
    )
    Divider()
    LastSearchList(lastSearches) { atLeastOneFilterHasChanged = true }

    Text(
        text = "${stringResource(id = R.string.all)} $filterLabel",
        fontSize = 10.sp,
        fontWeight = FontWeight.Black
    )
    Divider()
    FilterOptionsList(options) { atLeastOneFilterHasChanged = true }

    AppButton(
        isActive = atLeastOneFilterHasChanged,
        onClick = { },
        modifier = Modifier.padding(top = 34.dp),
        textRes = R.string.confirm_selection
    )
}

@Composable
fun LastSearchList(lastSearchs: Set<String>, onSelectFilter: () -> Unit) {

    val scrollState = rememberScrollState()

    LazyColumn(
        modifier = Modifier
            .padding(top = 15.dp)
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(lastSearchs.size) {

        }
    }

}

@Composable
fun FilterOptionsList(options: List<String>, onSelectFilter: () -> Unit) {

    val scrollState = rememberScrollState()

    LazyColumn(
        modifier = Modifier
            .padding(top = 15.dp)
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(options.size) {

        }
    }
}


@Composable
fun FilterCategory(
    filters: List<AppViewModel.Filter>, updateBooleanPref: (String, Boolean) -> Unit
) {

    var atLeastOneFilterHasChanged by remember {
        mutableStateOf(false)
    }

    Row() {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier.padding(end = 10.dp),
            painter = painterResource(id = R.drawable.filter),
            contentDescription = stringResource(
                id = R.string.cd_filter_icon
            ),
            tint = AppColor.Black
        )
        Text(
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            text = stringResource(
                id = R.string.filters_title
            )
        )

        if (atLeastOneFilterHasChanged) {
            Text(
                modifier = Modifier.weight(1f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = AppColor.BlueCamperPro,
                text = stringResource(
                    id = R.string.reset
                )
            )
        }
    }

    Text(
        modifier = Modifier.padding(top = 70.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Black,
        text = stringResource(
            id = R.string.category_subtitle
        )
    )
    Divider(modifier = Modifier.padding(vertical = 15.dp))

    filters.forEach {
        FilterItem(label = it.filterKey, onClick = { key, value ->
            atLeastOneFilterHasChanged = true
            updateBooleanPref(key, value)
        }, filterIsActive = it.selected)
    }

    Text(
        modifier = Modifier.padding(top = 70.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        text = stringResource(
            id = R.string.search_details_subtitle
        )
    )

    Divider(modifier = Modifier.padding(vertical = 15.dp))

    filters.forEach {
        atLeastOneFilterHasChanged = true
        FilterSearchBox(label = it.filterKey, onClick = { }, filterIsActive = it.selected)
    }

    AppButton(
        isActive = atLeastOneFilterHasChanged,
        onClick = { },
        modifier = Modifier.padding(top = 34.dp),
        textRes = R.string.apply_filters
    )
}

@Composable
fun FilterItem(label: String, onClick: (String, Boolean) -> Unit, filterIsActive: Boolean) {

    Row {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = label,
            color = if (filterIsActive) AppColor.BlueCamperPro else AppColor.unSelectedFilter
        )
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = filterIsActive,
            onCheckedChange = { onClick(label, it) },
            enabled = true,
            colors = CheckboxDefaults.colors(AppColor.BlueCamperPro)
        )
    }
}

@Composable
fun FilterSearchBox(label: String, onClick: (String) -> Unit, filterIsActive: Boolean) {

    val composableColor =
        if (filterIsActive) AppColor.selectedFilterOption else AppColor.unSelectedFilterOption

    Row(modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 12.dp)
        .border(
            BorderStroke(
                1.dp, composableColor
            )
        )
        .clickable(enabled = filterIsActive) { onClick(label) }) {
        Text(
            text = label, color = composableColor, modifier = Modifier.padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = stringResource(id = R.string.cd_filter_option, label),
            tint = composableColor
        )
    }
}