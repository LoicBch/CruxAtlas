package com.appmobiledition.laundryfinder.android.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appmobiledition.laundryfinder.android.LocalDependencyContainer
import com.appmobiledition.laundryfinder.android.R
import kotlinx.coroutines.launch

enum class FilterOptions {
    Revolution, Other
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetLayoutScreen() {

    val types = FilterOptions.values()
    val initialState = types.map { false }
    val checkedState = remember { mutableStateListOf(*initialState.toTypedArray()) }

    val sheetState = LocalDependencyContainer.current.appViewModel.bottomSheetIsShowing
    val appViewModel = LocalDependencyContainer.current.appViewModel
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            IconButton(onClick = {
                scope.launch {
                    sheetState.hide()
                }
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Type filter", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(modifier = Modifier.alpha(0f), onClick = {
                scope.launch {
                    sheetState.hide()
                }
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(types) { index, type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .height(48.dp)
                    ) {
                        Image(painterResource(id = R.drawable.marker), contentDescription = "")
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically),
                            text = types[index].name.lowercase()
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Checkbox(
                            checked = checkedState[index],
                            onCheckedChange = { checked ->
                                checkedState[index] = checked
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.Cyan,
                                uncheckedColor = Color.Gray
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                isActive = true,
                onClick = {
                    //                    appViewModel.applyFilter(
                    //                        checkedState.mapIndexed {a, b -> if (b) types[a] else null }.filterNotNull()
                    //                    )
                    scope.launch {
                        sheetState.hide()
                    }
                },
                modifier = Modifier.padding(16.dp),
                textRes = R.string.verified
            )
        }
    }

    // TODO: side effect
    //    val contentOption by LocalDependencyContainer.current.appViewModel.bottomSheetContent.collectAsState()


    // make several screen for each
    //    when (contentOption) {
    //        BottomSheetOption.FILTER -> FilterSheet()
    //        BottomSheetOption.FILTER_EVENT -> FilterEventsSheet()
    //        BottomSheetOption.SORT -> SortingSheet(contentOption)
    //        BottomSheetOption.SORT_AROUND_PLACE -> SortingSheet(contentOption)
    //        BottomSheetOption.SORT_EVENTS -> SortingSheet(contentOption)
    //        BottomSheetOption.MAPLAYER -> FilterSheet()
    //    }
}
