package com.example.camperpro.android

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.datastore.preferences.core.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.domain.model.Search
import com.example.camperpro.domain.usecases.AddSearch
import com.example.camperpro.domain.usecases.DeleteSearch
import com.example.camperpro.domain.usecases.GetAllSearchForACategory
import com.jetbrains.kmm.shared.data.ResultWrapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


//Deleguer ca
@OptIn(ExperimentalMaterialApi::class)
class AppViewModel(
    private val addSearchUsecase: AddSearch,
    private val deleteSearchUsecase: DeleteSearch,
    private val getAllSearchForACategory: GetAllSearchForACategory
) : ViewModel() {

    val bottomSheetIsShowing = ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val historicSearches = MutableStateFlow<MutableList<Search>>(mutableListOf())
    private val loadAroundMeIsPressedChannel = Channel<Boolean>()
    val loadAroundMeIsPressed = loadAroundMeIsPressedChannel.receiveAsFlow()

    fun onAroundMeClick() {
        viewModelScope.launch {
            loadAroundMeIsPressedChannel.send(true)
        }
    }

    fun addSearch(search: Search) {
        viewModelScope.launch {
            addSearchUsecase(search)
        }
    }

    fun deleteSearch(searchLabel: String) {
        viewModelScope.launch {
            deleteSearchUsecase(searchLabel)
        }
    }

    fun getSearchesOfCategory(searchCategory: String) {
        viewModelScope.launch {
            when (val res = getAllSearchForACategory(searchCategory)) {
                is ResultWrapper.Failure -> TODO()
                is ResultWrapper.Success -> historicSearches.update { res.value!!.toMutableList() }
            }
        }
    }
}

data class Filter(val filterKey: String, val selected: Boolean)