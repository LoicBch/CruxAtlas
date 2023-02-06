package com.example.camperpro.android.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.filter.FilterEventsSheet
import com.example.camperpro.android.filter.FilterSheet
import com.example.camperpro.android.mainmap.SortingSheet
import com.example.camperpro.utils.BottomSheetOption

@Composable
fun BottomSheetLayoutScreen() {

    // TODO: side effect
    val contentOption by LocalDependencyContainer.current.appViewModel.bottomSheetContent.collectAsState()


    // make several screen for each
    when (contentOption) {
        BottomSheetOption.FILTER -> FilterSheet()
        BottomSheetOption.FILTER_EVENT -> FilterEventsSheet()
        BottomSheetOption.SORT -> SortingSheet(contentOption)
        BottomSheetOption.SORT_AROUND_PLACE -> SortingSheet(contentOption)
        BottomSheetOption.SORT_EVENTS -> SortingSheet(contentOption)
        BottomSheetOption.MAPLAYER -> FilterSheet()
    }
}
