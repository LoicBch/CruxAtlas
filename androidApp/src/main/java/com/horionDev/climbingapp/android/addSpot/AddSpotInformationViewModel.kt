//package com.horionDev.climbingapp.android.addSpot
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.horion.murbex.data.ResultWrapper
//import com.horion.murbex.domain.model.Spot
//import com.horion.murbex.domain.usecases.AddSpotUseCase
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.asSharedFlow
//import kotlinx.coroutines.launch
//
//class AddSpotInformationViewModel(
//    private val addSpotUseCase: AddSpotUseCase
//) : ViewModel() {
//
//    private val _event = MutableSharedFlow<AddSpotInfoEvent>()
//    val event = _event.asSharedFlow()
//
//    fun addSpot(spot: Spot) {
//        viewModelScope.launch {
//            when (val result = addSpotUseCase(spot)) {
//                is ResultWrapper.Success -> {
//                    _event.emit(AddSpotInfoEvent.NavigateToThanksYou(result.value!!))
//                }
//                is ResultWrapper.Failure -> {
//
//                }
//            }
//        }
//    }
//
//    sealed class AddSpotInfoEvent {
//        data class NavigateToThanksYou(var spot: Spot) : AddSpotInfoEvent()
//    }
//}