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

    init {
        getChecklists()
    }

    private var checklistsGlobal = listOf<CheckList>()

    private var tags = mutableStateListOf<Pair<String, Boolean>>()
    private val _tags = MutableStateFlow(tags)
    val tagsFlow: StateFlow<List<Pair<String, Boolean>>> get() = _tags

    val loading = savedStateHandle.getStateFlow("loading", false)
    val tagSelected = savedStateHandle.getStateFlow("tagSelected", Pair("", false))
    val checklistsShowed = savedStateHandle.getStateFlow("checklists", listOf<CheckList>())

    fun selectTag(name: String) {

        val index = tags.indexOf(tags.find { it.first == name })
        val selectedFilterNames = mutableStateListOf<String>()

        if (tags[index].second) {
            for (i in tags.indices) {
                if (tags[i].first == name) {
                    tags[i] = tags[i].copy(second = false)
                }
            }
        } else {
            tags[index] = tags[index].copy(second = true)
        }

        for (i in tags.indices) {
            if (tags[i].second) {
                selectedFilterNames.add(tags[i].first)
            }
        }

        if (selectedFilterNames.isEmpty()) {
            savedStateHandle["checklists"] = checklistsGlobal
        } else {
            savedStateHandle["checklists"] =
                checklistsGlobal.filter { checkIfCommonValue(selectedFilterNames, it.tags) }
        }
    }

    private fun checkIfCommonValue(tab1: List<String>, tab2: List<String>): Boolean {
        for (element1 in tab1) {
            for (element2 in tab2) {
                if (element1 == element2) {
                    return true
                }
            }
        }
        return false
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