package com.example.camperpro.android.aroundLocation

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Colors
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.SearchField
import com.example.camperpro.android.ui.theme.Dimensions
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AroundLocationScreen() {

    Column(
        modifier = Modifier.padding(15.dp).background(Color.White)
    ) {
        Row(Modifier.fillMaxWidth().padding(bottom = 40.dp)) {
            Image(
                painter = painterResource(id = R.drawable.around_location_selected),
                contentDescription = stringResource(id = R.string.cd_magnifying_glass_icon),
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = stringResource(id = R.string.appbar_around_location), fontWeight = FontWeight
                    .Black, fontSize = Dimensions.bigTitle, modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        SearchField(Modifier, placeHolder = R.string
                .around_location_placeholder
        )

        Text(
            modifier = Modifier.padding(top = 25.dp),
            text = stringResource(id = R.string.last_searched), fontSize = 16.sp, fontWeight =
            FontWeight.SemiBold
        )
        Divider(modifier = Modifier.padding(top = 5.dp))
        LastSearchList(lastSearchs = emptySet()) {
            
        }
    }
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
