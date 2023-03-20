package com.example.camperpro.android

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.datastore.preferences.core.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.android.composables.GlobalPopupState
import com.example.camperpro.android.composables.GlobalSliderState
import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.Search
import com.example.camperpro.domain.usecases.AddSearch
import com.example.camperpro.domain.usecases.DeleteSearch
import com.example.camperpro.domain.usecases.GetAllSearchForACategory
import com.example.camperpro.managers.location.LocationManager
import com.example.camperpro.utils.BottomSheetOption
import com.example.camperpro.utils.ConnectivityObserver
import com.example.camperpro.utils.Globals
import com.example.camperpro.utils.SortOption
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// TODO: make a viewmodel specific to bottomSheetScreen and keep this viewModel for commons data
@OptIn(ExperimentalMaterialApi::class)
class AppViewModel(
    private val addSearchUsecase: AddSearch,
    private val deleteSearchUsecase: DeleteSearch,
    private val getAllSearchForACategory: GetAllSearchForACategory
) : ViewModel() {

    val bottomSheetIsShowing = ModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        isSkipHalfExpanded = true
    )
    val historicSearches = MutableStateFlow<MutableList<Search>>(mutableListOf())
    private val loadAroundMeIsPressedChannel = Channel<Boolean>()
    val loadAroundMeIsPressed = loadAroundMeIsPressedChannel.receiveAsFlow()

    private val _locationIsObserved = MutableStateFlow(false)
    val locationIsObserved = _locationIsObserved.asStateFlow()

    private val _networkIsObserved = MutableStateFlow(false)
    val networkIsObserved = _networkIsObserved.asStateFlow()

    private val _bottomSheetContent = MutableStateFlow(BottomSheetOption.FILTER)
    val bottomSheetContent = _bottomSheetContent.asStateFlow()

    private val _verticalListSortingOption = MutableStateFlow(SortOption.NONE)
    val verticalListSortingOption = _verticalListSortingOption.asStateFlow()

    private val _globalPopup = MutableStateFlow(GlobalPopupState.HID)
    val globalPopup = _globalPopup.asStateFlow()

    private val _globalSlider = MutableStateFlow(mutableListOf<GlobalSliderState>())
    val globalSlider = _globalSlider.asStateFlow()

    val observersAreSet =
        combine(locationIsObserved, networkIsObserved) { locationIsObserved, networkIsObserved ->
            locationIsObserved && networkIsObserved
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun showGlobalPopup(state: GlobalPopupState) {
        _globalPopup.update { state }
    }

    fun showGlobalSlider(state: GlobalSliderState) {
        val newList = globalSlider.value.toMutableList()
        newList.add(state)
        _globalSlider.update { newList }
    }

    fun onAroundMeClick() {
        viewModelScope.launch {
            loadAroundMeIsPressedChannel.send(true)
        }
    }

    fun onBottomSheetContentChange(option: BottomSheetOption) {
        _bottomSheetContent.update { option }
    }

    fun onLocationObserveStarted() {
        _locationIsObserved.update { true }
    }

    fun onNetworkObserveStarted() {
        _networkIsObserved.update { true }
    }

    fun onSortingOptionSelected(sortOption: SortOption) {
        _verticalListSortingOption.update { sortOption }
    }

    fun addSearch(search: Search) {
        viewModelScope.launch {
            addSearchUsecase(search)
        }
    }

    fun deleteSearch(search: Search) {
        viewModelScope.launch {
            deleteSearchUsecase(search)
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

    fun hideGlobalPopup() {
        _globalPopup.update { GlobalPopupState.HID }
    }

    fun removeGpsMissingNotification() {
        Log.d("LOCATION", "removeGpsMissingNotification: ")
        val newList = globalSlider.value.toMutableList()
        newList.remove(GlobalSliderState.GPS_MISSING)
        _globalSlider.update {
            newList
        }
    }

    fun removeNetworkMissingNotification() {
        val newList = globalSlider.value.toMutableList()
        newList.remove(GlobalSliderState.NETWORK_MISSING)
        _globalSlider.update {
            newList
        }
    }

    fun hideGlobalSlider() {
        _globalSlider.update { globalSlider.value.apply { clear() } }
    }

    fun withNetworkOnly(networkAvailableBlock: () -> Unit) {
        if (Globals.network.status == ConnectivityObserver.NetworkStatus.Available) {
            Log.d("NETWORK", networkAvailableBlock.invoke().toString())
            networkAvailableBlock.invoke()
        } else {
            _globalPopup.update { GlobalPopupState.NETWORK_MISSING }
        }
    }

    fun withGpsOnly(gpsAvailableBlock: () -> Unit) {
        if (LocationManager.isLocationEnable()) {
            gpsAvailableBlock()
        } else {
            _globalPopup.update { GlobalPopupState.GPS_MISSING }
        }
    }

}