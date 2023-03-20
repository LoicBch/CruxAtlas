package com.example.camperpro.android.checklists

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.camperpro.domain.model.CheckList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CheckListsViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {


    private var tags = mutableStateListOf<Pair<String, Boolean>>()
    private val _tags = MutableStateFlow(tags)
    val tagsFlow: StateFlow<List<Pair<String, Boolean>>> get() = _tags

    val tagSelected = savedStateHandle.getStateFlow("tags", Pair("", false))
    val checklists = savedStateHandle.getStateFlow("checklists", listOf<CheckList>())

    fun selectTag(name: String) {
        tags.forEachIndexed() { index, _ ->
            tags[index] = tags[index].copy(second = false)
        }

        val index = tags.indexOf(tags.find { it.first == name })
        tags[index] = tags[index].copy(second = true)
    }
}