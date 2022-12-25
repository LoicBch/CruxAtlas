package com.example.camperpro.android.aroundLocation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.SearchField
import com.example.camperpro.android.filter.LastSearchItem
import com.example.camperpro.android.ui.theme.AppColor
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AroundLocationScreen() {

    Column(
        modifier = Modifier
            .padding(15.dp)
            .background(Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp), verticalAlignment = Alignment.CenterVertically
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
                .around_location_placeholder
        )

        Text(
            modifier = Modifier.padding(top = 54.dp),
            text = stringResource(id = R.string.last_searched), fontSize = 14.sp, fontWeight =
            FontWeight.W500, color = Color.Black
        )
        Divider(modifier = Modifier.padding(top = 12.dp))
        //        launch location search
        LastSearchedLocationList("AROUND_LOCATION", { TODO() })
    }
}

@Composable
fun LastSearchedLocationList(categorySelected: String, onSelectFilter: (String) -> Unit) {

    val appViewmodel = LocalDependencyContainer.current.appViewModel
    val searches by appViewmodel.historicSearches.collectAsState()
    val scrollState = rememberScrollState()

    appViewmodel.getSearchesOfCategory(categorySelected)

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
        items(searches) { search ->
            LastSearchItem(onSearchDelete = {
                searches.remove(search)
                appViewmodel.deleteSearch(search)
            }, onSelectSearch = {
                onSelectFilter(it)
            }, search = search)
        }
    }
}
