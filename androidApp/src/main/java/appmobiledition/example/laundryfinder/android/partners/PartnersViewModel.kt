package com.example.camperpro.android.partners

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.Partner
import com.example.camperpro.domain.usecases.FetchPartners
import kotlinx.coroutines.launch

class PartnersViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchPartners: FetchPartners,
) : ViewModel() {

    // TODO: state hosting

    val partners = savedStateHandle.getStateFlow("partners", emptyList<Partner>())
    private val loading = savedStateHandle.getStateFlow("loading", false)

    fun loadPartners() {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchPartners()) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
                is ResultWrapper.Success -> {
                    savedStateHandle["partners"] = call.value
                    savedStateHandle["loading"] = false
                }
            }
        }
    }
}