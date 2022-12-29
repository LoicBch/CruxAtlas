package com.example.camperpro.android.aroundLocation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.domain.model.Location
import com.example.camperpro.domain.usecases.FetchSuggestionsFromInput
import com.example.camperpro.utils.Constants
import com.jetbrains.kmm.shared.data.ResultWrapper
import kotlinx.coroutines.launch

class AroundLocationViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchSuggestionsFromInput: FetchSuggestionsFromInput
) : ViewModel() {

    val suggestionList =
        savedStateHandle.getStateFlow("suggestionsList", emptyList<Pair<String, Location>>())

    fun onUserSearch(search: String) {
        if (search.length > Constants.SUGGESTION_START_LENGTH) {
            viewModelScope.launch {
                when (val result = fetchSuggestionsFromInput(search)) {
                    is ResultWrapper.Failure -> TODO()
                    is ResultWrapper.Success -> savedStateHandle["suggestionList"] = result.value
                }
            }
        }else{
            savedStateHandle["suggestionList"] = emptyList<Pair<String, Location>>()
        }
    }

}