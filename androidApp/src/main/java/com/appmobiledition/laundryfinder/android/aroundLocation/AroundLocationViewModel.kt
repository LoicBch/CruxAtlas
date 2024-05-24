package com.appmobiledition.laundryfinder.android.aroundLocation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appmobiledition.laundryfinder.domain.model.composition.Location
import com.appmobiledition.laundryfinder.domain.model.Place
import com.appmobiledition.laundryfinder.domain.model.Search
import com.appmobiledition.laundryfinder.domain.usecases.AddSearch
import com.appmobiledition.laundryfinder.domain.usecases.DeleteSearch
import com.appmobiledition.laundryfinder.domain.usecases.FetchSuggestionsFromInput
import com.appmobiledition.laundryfinder.domain.usecases.GetAllSearchForACategory
import com.appmobiledition.laundryfinder.utils.Constants
import com.appmobiledition.laundryfinder.data.ResultWrapper
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