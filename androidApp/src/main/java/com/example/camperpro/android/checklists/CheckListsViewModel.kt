package com.example.camperpro.android.checklists

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.CheckList
import com.example.camperpro.domain.usecases.FetchCheckLists
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckListsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchCheckLists: FetchCheckLists
) : ViewModel() {

    private var checklistsGlobal = listOf<CheckList>()

    private var tags = mutableStateListOf<Pair<String, Boolean>>()
    private val _tags = MutableStateFlow(tags)
    val tagsFlow: StateFlow<List<Pair<String, Boolean>>> get() = _tags

    val loading = savedStateHandle.getStateFlow("loading", false)
    val tagSelected = savedStateHandle.getStateFlow("tagSelected", Pair("", false))
    val checklistsShowed = savedStateHandle.getStateFlow("checklists", listOf<CheckList>())

    fun selectTag(name: String) {
        val index = tags.indexOf(tags.find { it.first == name })
        if (tags[index].second) {
            for (i in tags.indices) {
                tags[i] = tags[i].copy(second = false)
            }
        } else {
            for (i in tags.indices) {
                tags[i] = tags[i].copy(second = false)
            }
            tags[index] = tags[index].copy(second = true)
        }
        savedStateHandle["checklists"] = checklistsGlobal.filter { it.tags.contains(name) }
    }

    fun getChecklists() {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val res = fetchCheckLists()) {
                is ResultWrapper.Failure -> {

                }
                is ResultWrapper.Success -> {
                    savedStateHandle["checklists"] = res.value
                    checklistsGlobal = res.value!!
                    savedStateHandle["loading"] = false
                    tags.addAll(
                        res.value!!.map { it.tags }
                            .flatten()
                            .distinct()
                            .map { Pair(it, false) })
                }
            }
        }
    }
}