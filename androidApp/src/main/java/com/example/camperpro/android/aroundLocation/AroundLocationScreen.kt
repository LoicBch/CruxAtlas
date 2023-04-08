package com.example.camperpro.android.aroundLocation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.SearchField
import com.example.camperpro.android.composables.collectAsStateWithLifecycleImmutable
import com.example.camperpro.android.filter.LastSearchItem
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.domain.model.Place
import com.example.camperpro.domain.model.Search
import com.example.camperpro.utils.Constants
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun AroundLocationScreen(
    resultNavigator: ResultBackNavigator<Place>,
    viewModel: AroundLocationViewModel = getViewModel()
) {
    val suggestionsList by viewModel.suggestionList.collectAsStateWithLifecycleImmutable()
    val placesHistoric = viewModel.placesHistoric

    val onUserSearch: (value: String) -> Unit = remember {
        return@remember viewModel::onUserSearch
    }

    val onSelectPlace: (value: Search) -> Unit = remember {
        return@remember viewModel::onSelectPlace
    }

    val onDeleteSearch: (value: Search) -> Unit = remember {
        return@remember viewModel::onDeleteSearch
    }

    LaunchedEffect(true) {
        viewModel.loadPlacesHistoric()
    }

    LaunchedEffect(viewModel.placeSelected) {
        viewModel.placeSelected.collect {
            if (viewModel.placeSelected.value.name != "") {
                resultNavigator.navigateBack(
                    result = it
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(15.dp)
            .background(Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.around_location_selected),
                contentDescription = stringResource(id = R.string.cd_magnifying_glass_icon),
                tint = Color.Unspecified
            )

            Text(
                text = stringResource(id = R.string.appbar_around_location),
                fontWeight = FontWeight.W700,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.around_location_subtitle),
            fontWeight = FontWeight(450),
            fontSize = 12.sp,
            color = AppColor.neutralText
        )

        SearchField(
            Modifier.padding(top = 12.dp), placeHolder = R.string
                .around_location_placeholder, onUserSearch = {
                onUserSearch(it.text)
            }
        )

        if (suggestionsList.value.isEmpty()) {

            Text(
                modifier = Modifier.padding(top = 54.dp),
                text = stringResource(id = R.string.last_searched), fontSize = 14.sp, fontWeight =
                FontWeight.W500, color = Color.Black
            )

            Divider(modifier = Modifier.padding(top = 12.dp))

            LastSearchedLocationList(
                placesHistoric,
                { onDeleteSearch(it) },
                { onSelectPlace(it) })

        } else {
            SuggestionsList(list = suggestionsList.value) {
                val search = Search(
                    Constants.Persistence.SEARCH_CATEGORY_LOCATION,
                    it.name,
                    System.currentTimeMillis(),
                    it.location.latitude,
                    it.location.longitude
                )
                onSelectPlace(search)
            }
        }
    }
}

@Composable
fun LastSearchedLocationList(
    placeHistoric: StateFlow<List<Search>>,
    onDeleteSearch: (Search) -> Unit,
    onSelectPlace: (Search) -> Unit
) {

    val searches by placeHistoric.collectAsState()
    val scrollState = rememberScrollState()

    LazyColumn(
        modifier = Modifier
            .padding(top = 22.dp)
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(searches) { search ->
            LastSearchItem(onSearchDelete = {
                onDeleteSearch(search)
            }, onSelectSearch = { searchLabelSelected ->
                onSelectPlace(searches[searches.indexOfFirst { it.searchLabel == searchLabelSelected }])
            }, search = search.searchLabel)
        }
    }
}

@Composable
fun SuggestionsList(
    list: List<Place>,
    onItemClicked: (Place) -> Unit
) {

    val scrollState = rememberScrollState()

    LazyColumn(
        modifier = Modifier
            .padding(top = 12.dp)
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(list) { search ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { onItemClicked(search) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = search.name,
                    fontWeight = FontWeight(450),
                    fontSize = 18.sp,
                    color = AppColor.suggestionsLabel
                )
            }
        }
    }
}

