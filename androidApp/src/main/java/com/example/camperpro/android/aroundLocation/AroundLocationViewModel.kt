package com.example.camperpro.android.aroundLocation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.Place
import com.example.camperpro.domain.model.Search
import com.example.camperpro.domain.usecases.AddSearch
import com.example.camperpro.domain.usecases.DeleteSearch
import com.example.camperpro.domain.usecases.FetchSuggestionsFromInput
import com.example.camperpro.domain.usecases.GetAllSearchForACategory
import com.example.camperpro.utils.Constants
import com.example.camperpro.data.ResultWrapper
import kotlinx.coroutines.launch

class AroundLocationViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchSuggestionsFromInput: FetchSuggestionsFromInput,
    private val addSearchUsecase: AddSearch,
    private val deleteSearchUsecase: DeleteSearch,
    private val getAllSearchForACategory: GetAllSearchForACategory
) : ViewModel() {

    val suggestionList =
        savedStateHandle.getStateFlow("suggestionList", emptyList<Place>())

    val placesHistoric = savedStateHandle.getStateFlow("placesHistoric", emptyList<Search>())
    val placeSelected =
        savedStateHandle.getStateFlow("placeSelected", Place("", Location(0.0, 0.0)))

    fun onUserSearch(search: String) {
        if (search.length > Constants.SUGGESTION_START_LENGTH) {
            viewModelScope.launch {
                when (val result = fetchSuggestionsFromInput(search)) {
                    is ResultWrapper.Failure -> savedStateHandle["suggestionList"] =
                        emptyList<Place>()
                    is ResultWrapper.Success -> savedStateHandle["suggestionList"] = result.value
                }
            }
        } else {
            savedStateHandle["suggestionList"] = emptyList<Place>()
        }
    }

    fun onSelectPlace(searchSelected: Search) {
        viewModelScope.launch {
            addSearchUsecase(searchSelected)
            savedStateHandle["placeSelected"] = Place(
                searchSelected.searchLabel,
                Location(searchSelected.lat!!, searchSelected.lon!!)
            )
        }
    }

    fun onDeleteSearch(search: Search) {
        viewModelScope.launch {
            deleteSearchUsecase(search)
            val updatedList = placesHistoric.value.toMutableList()
                .apply { this.removeIf { it.searchLabel == search.searchLabel } }
            savedStateHandle["placesHistoric"] = updatedList.toList()
        }
    }

    fun loadPlacesHistoric() {
        viewModelScope.launch {
            when (val res =
                getAllSearchForACategory(Constants.Persistence.SEARCH_CATEGORY_LOCATION)) {
                is ResultWrapper.Failure -> savedStateHandle["placesHistoric"] = emptyList<Search>()
                is ResultWrapper.Success -> savedStateHandle["placesHistoric"] = res.value
            }
        }
    }

}