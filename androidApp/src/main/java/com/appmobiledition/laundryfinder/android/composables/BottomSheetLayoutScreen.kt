package com.appmobiledition.laundryfinder.android.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.appmobiledition.laundryfinder.android.LocalDependencyContainer
import com.appmobiledition.laundryfinder.android.filter.FilterEventsSheet
import com.appmobiledition.laundryfinder.android.filter.FilterSheet
import com.appmobiledition.laundryfinder.android.mainmap.SortingSheet
import com.appmobiledition.laundryfinder.utils.BottomSheetOption
import com.appmobiledition.laundryfinder.utils.Globals

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
