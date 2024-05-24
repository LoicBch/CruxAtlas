package com.example.camperpro.android.extensions

import android.util.Log
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*

val LazyListState.lastVisibleItemIndex get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun LazyListState.scrollByGestureEnded(): Boolean {
    val currentGesture by interactionSource.interactions.collectAsState(
        initial = DragInteraction.Stop(DragInteraction.Start())
    )

    var scrollWasInitiatedByGesture by remember {
        mutableStateOf(false)
    }

    return remember(isScrollInProgress) {

        Log.d("GESTURE", isScrollInProgress.toString())

        derivedStateOf {
            if (isScrollInProgress) {
                if (currentGesture is DragInteraction.Start) {
                    scrollWasInitiatedByGesture = true
                }
                //                Log.d("GESTURE", "Dont start another animate")
                false
            } else {
                if (scrollWasInitiatedByGesture) {
                    scrollWasInitiatedByGesture = false
                    //                    Log.d("GESTURE", "start another animate")
                    true
                } else {
                    //                    Log.d("GESTURE", "Dont start another animate")
                    false
                }
            }
        }.value
    }
}