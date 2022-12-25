package com.example.camperpro.android.composables

import androidx.compose.runtime.Composable
import com.example.camperpro.android.filter.FilterSheet
import com.example.camperpro.android.mainmap.SortingSheet
import com.example.camperpro.utils.BottomSheetOption
import com.example.camperpro.utils.Globals

@Composable
fun BottomSheetLayoutScreen() {
    when(Globals.currentBottomSheetOption){
        BottomSheetOption.FILTER -> FilterSheet()
        BottomSheetOption.SORT -> SortingSheet()
        BottomSheetOption.MAPLAYER -> FilterSheet()
    }
}
