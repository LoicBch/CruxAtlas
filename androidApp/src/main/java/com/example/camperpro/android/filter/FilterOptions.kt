package com.example.camperpro.android.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.FilterSearchField
import com.example.camperpro.android.composables.HistoricSearchList
import com.example.camperpro.android.composables.ResultsList
import com.example.camperpro.android.composables.SearchField
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.utils.FilterType
import com.example.camperpro.utils.Globals

@Composable
fun FilterOptions(
    onItemClick: (String) -> Unit,
    onSelectingOptionCancel: () -> Unit,
    categorySelected: FilterType
) {

    val textState = remember { mutableStateOf(TextFieldValue()) }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { onSelectingOptionCancel() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "",
                    tint = AppColor.Tertiary
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(
                    id = when (categorySelected) {
                        FilterType.BRAND -> R.string.motorhome_brands
                        FilterType.SERVICE -> R.string.motorhome_services
                        FilterType.COUNTRIES -> R.string.filter_step1_option1
                        FilterType.UNSELECTED_DEALER -> R.string.filter_step1_option1
                        FilterType.UNSELECTED_EVENT -> TODO()
                    }
                ),
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))

            IconButton(modifier = Modifier.alpha(0f), onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "",
                    tint = AppColor.Tertiary
                )
            }
        }

        FilterSearchField(
            modifier = Modifier.padding(top = 22.dp),
            placeHolder = R.string.search_placeholder,
            onUserSearch = { textState.value = it }
        )

        if (textState.value.text.isEmpty()) {
            HistoricSearchList(categorySelected, onSelectFilter = onItemClick)
        }

        val label = if (textState.value.text.isEmpty()) {
            when (categorySelected) {
                FilterType.SERVICE -> stringResource(id = R.string.all_services_type)
                FilterType.BRAND -> stringResource(id = R.string.all_motorhome_brand)
                else -> {
                    ""
                }
            }
        } else {
            stringResource(id = R.string.search_result)
        }

        val items = if (textState.value.text.isEmpty()) {
            when (label) {
                stringResource(id = R.string.all_motorhome_brand) -> Globals.filters.brands.map { it.second }
                stringResource(id = R.string.all_services_type) -> Globals.filters.services.map { it.second }
                else -> {
                    emptyList()
                }
            }
        } else {
            makeListFromInput(textState.value, categorySelected).take(5)
        }

        ResultsList(onItemClick, label, items)
    }
}

fun makeListFromInput(value: TextFieldValue, categorySelected: FilterType): List<String> {
    val listToCompare = when (categorySelected) {
        FilterType.SERVICE -> Globals.filters.services
        FilterType.BRAND -> Globals.filters.brands
        FilterType.UNSELECTED_DEALER -> emptyList()
        else -> {
            emptyList()
        }
    }

    return listToCompare.map { it.second }.filter {
        it.lowercase().startsWith(value.text.lowercase())
    }
}


@Composable
fun OptionItem(modifier: Modifier, option: String) {
    Row(
        modifier = modifier
            .height(Dimensions.buttonHeight)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "",
            tint = AppColor.Tertiary
        )

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = option,
            fontSize = 16.sp,
            fontWeight = FontWeight(450)
        )
    }
}
