package com.appmobiledition.laundryfinder.android

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.datastore.preferences.core.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appmobiledition.laundryfinder.android.composables.GlobalPopupState
import com.appmobiledition.laundryfinder.android.composables.GlobalSliderState
import com.appmobiledition.laundryfinder.data.ResultWrapper
import com.appmobiledition.laundryfinder.domain.model.Search
import com.appmobiledition.laundryfinder.domain.model.composition.Filter
import com.appmobiledition.laundryfinder.domain.model.composition.UpdateSource
import com.appmobiledition.laundryfinder.domain.model.composition.filterName
import com.appmobiledition.laundryfinder.domain.model.composition.getIdFromFilterName
import com.appmobiledition.laundryfinder.domain.usecases.*
import com.appmobiledition.laundryfinder.managers.location.LocationManager
import com.appmobiledition.laundryfinder.utils.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// TODO: make a viewmodel specific to bottomSheetScreen and keep this viewModel for commons data
// todo same for filters and search
@OptIn(ExperimentalMaterialApi::class)
class AppViewModel(
    private val addSearchUsecase: AddSearch,
    private val deleteSearchUsecase: DeleteSearch,
    private val getAllSearchForACategory: GetAllSearchForACategory,
    private val applyPlacesFilters: ApplyPlacesFilters,
    private val deleteFilter: DeleteFilter,
    private val getFiltersSaved: GetFiltersSaved,
) : ViewModel() {

    val bottomSheetIsShowing = ModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, isSkipHalfExpanded = true
    )
    val historicSearches = MutableStateFlow<MutableList<Search>>(mutableListOf())
    private val loadAroundMeIsPressedChannel = Channel<Boolean>()
    val loadAroundMeIsPressed = loadAroundMeIsPressedChannel.receiveAsFlow()

    private val filtersAppliedChannel = Channel<FilterType>()
    val filtersApplied = filtersAppliedChannel.receiveAsFlow()

    private val _filterDealerSelected = MutableStateFlow(Filter())
    val filterDealerSelected = _filterDealerSelected.asStateFlow()

    private val _filtersDealerUsed = MutableStateFlow(listOf<Filter>())
    val filtersDealerUsed = _filtersDealerUsed.asStateFlow()

    private val _filterEventSelected = MutableStateFlow(Filter())
    val filterEventSelected = _filterEventSelected.asStateFlow()

    private val _filtersEventUsed = MutableStateFlow(listOf<Filter>())
    val filtersEventUsed = _filtersEventUsed.asStateFlow()

    private val _locationIsObserved = MutableStateFlow(false)
    val locationIsObserved = _locationIsObserved.asStateFlow()

    private val _networkIsObserved = MutableStateFlow(false)
    val networkIsObserved = _networkIsObserved.asStateFlow()

    private val _bottomSheetContent = MutableStateFlow(BottomSheetOption.FILTER)
    val bottomSheetContent = _bottomSheetContent.asStateFlow()

    private val _verticalListSortingOption = MutableStateFlow(SortOption.NONE)
    val verticalListSortingOption = _verticalListSortingOption.asStateFlow()

    private val _filtersUpdated = MutableStateFlow(false)
    val filtersUpdated = _filtersUpdated.asStateFlow()

    private val _globalPopup = MutableStateFlow(GlobalPopupState.HID)
    val globalPopup = _globalPopup.asStateFlow()

    private val _globalSlider = MutableStateFlow(mutableListOf<GlobalSliderState>())
    val globalSlider = _globalSlider.asStateFlow()

    private val _sortingDealerApplied = MutableStateFlow(SortOption.NONE)
    val sortingDealerApplied = _sortingDealerApplied.asStateFlow()

    private val _sortingEventApplied = MutableStateFlow(SortOption.NONE)
    val sortingEventApplied = _sortingEventApplied.asStateFlow()

    private val _eventsAreDisplayed = MutableStateFlow(false)
    val eventsAreDisplayed = _eventsAreDisplayed.asStateFlow()


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

    fun onEventDisplayedChange(isDisplayed: Boolean) {
        _eventsAreDisplayed.update { isDisplayed }
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

    fun removeFilter(filter: Filter) {

        when (filter.category) {
            FilterType.COUNTRIES -> {
                _filtersEventUsed.update {
                    _filtersEventUsed.value.toMutableList().apply { remove(filter) }
                }
            }
            FilterType.UNSELECTED_DEALER -> {}
            FilterType.UNSELECTED_EVENT -> {}
            else -> {
                _filtersDealerUsed.update {
                    _filtersDealerUsed.value.toMutableList().apply { remove(filter) }
                }
            }
        }

        viewModelScope.launch {
            deleteFilter(filter)
        }
    }

    fun applyFilterToDealers() {

        if (filterDealerSelected.value.category != FilterType.UNSELECTED_DEALER) {
            _filtersDealerUsed.update {
                _filtersDealerUsed.value.toMutableList().apply { add(_filterDealerSelected.value) }
            }
        }

        viewModelScope.launch {
            applyPlacesFilters(
                Filter(
                    _filterDealerSelected.value.category, _filterDealerSelected.value.filterId, true
                )
            )
            filtersAppliedChannel.send(_filterEventSelected.value.category)
        }
    }

    fun applyFilterToEvents(countrySelected: String) {

        _filterEventSelected.update {
            Filter(
                _filterEventSelected.value.category,
                countrySelected,
                true
            )
        }

        if (filterEventSelected.value.filterId != "") {
            _filtersEventUsed.update {
                _filtersEventUsed.value.toMutableList().apply { add(_filterEventSelected.value) }
            }
        }

        viewModelScope.launch {
            applyPlacesFilters(
                Filter(
                    _filterEventSelected.value.category, _filterEventSelected.value.filterId, true
                )
            )
            filtersAppliedChannel.send(FilterType.COUNTRIES)
        }
    }

    fun getFilter() {
        viewModelScope.launch {
            when (val result = getFiltersSaved()) {
                is ResultWrapper.Failure -> {

                }
                is ResultWrapper.Success -> {
                    val dealerFilters =
                        result.value!!.filter { it.category != FilterType.COUNTRIES }
                    val eventFilters = result.value!!.filter { it.category == FilterType.COUNTRIES }

                    if (dealerFilters.any { it.isSelected }) {
                        _filterDealerSelected.update { result.value!!.first { it.isSelected } }
                    }

                    if (eventFilters.any { it.isSelected }) {
                        _filterEventSelected.update { result.value!!.first { it.isSelected } }
                    }

                    _filtersDealerUsed.update { dealerFilters.filter { !it.isSelected } }
                    _filtersEventUsed.update { eventFilters.filter { !it.isSelected } }
                }
            }
        }
    }

    fun isFiltersActive(updateSource: UpdateSource): Boolean {
        return when (updateSource) {
            UpdateSource.AROUND_ME -> {
                _filterDealerSelected.value.filterName != ""
            }
            UpdateSource.EVENTS -> {
                _filterEventSelected.value.filterName != ""
            }
            else -> {
                _filterDealerSelected.value.filterName != ""
            }
        }
    }

    fun isSortingActive(): Boolean {
        return _verticalListSortingOption.value != SortOption.NONE
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

    fun onFilterCategorySelected(category: FilterType) {
        _filterDealerSelected.update {
            Filter(category, "")
        }
    }

    fun onFilterOptionSelected(filterName: String) {
        _filterDealerSelected.update {
            Filter(
                _filterDealerSelected.value.category,
                _filterDealerSelected.value.getIdFromFilterName(filterName)?: ""
            )
        }
    }

}