package com.horionDev.climbingapp.android

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.android.composables.GlobalPopupState
import com.horionDev.climbingapp.android.composables.GlobalSliderState
import com.horionDev.climbingapp.android.parcelable.GradeNotation
import com.horionDev.climbingapp.managers.location.LocationManager
import com.horionDev.climbingapp.utils.BottomSheetOption
import com.horionDev.climbingapp.utils.ConnectivityObserver
import com.horionDev.climbingapp.utils.Constants
import com.horionDev.climbingapp.utils.Globals
import com.horionDev.climbingapp.utils.KMMPreference
import com.horionDev.climbingapp.utils.SortOption
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// TODO: make a viewmodel specific to bottomSheetScreen and keep this viewModel for commons data
// todo same for filters and search
@OptIn(ExperimentalMaterialApi::class)
class AppViewModel(private val kmmPreference: KMMPreference) : ViewModel() {

    val bottomSheetIsShowing = ModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, isSkipHalfExpanded = true
    )
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
//        val newList = globalSlider.value.toMutableList()
//        newList.add(state)
//        _globalSlider.update { newList }
    }

    fun onAroundMeClick() {
        viewModelScope.launch {
            loadAroundMeIsPressedChannel.send(true)
        }
    }

    fun onLoadingChange(isLoading: Boolean) {
//        _loading.update { isLoading }
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

    fun getCurrentGradesSystem(): GradeNotation {
        val notation = kmmPreference.getInt(Constants.PreferencesKey.GRADING_SYSTEM, -1)
        return when (notation) {
            Constants.AMERICAN_YDS -> GradeNotation.UIAA
            Constants.FRENCH ->  GradeNotation.French
            Constants.UIAA ->  GradeNotation.YDS
            else -> GradeNotation.YDS
        }
    }

    fun onSortingOptionSelected(sortOption: SortOption) {
        _verticalListSortingOption.update { sortOption }
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