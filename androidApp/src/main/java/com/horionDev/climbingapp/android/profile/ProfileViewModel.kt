package com.horionDev.climbingapp.android.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.dto.UserDto
import com.horionDev.climbingapp.data.repositories.Users
import com.horionDev.climbingapp.domain.model.entities.BoulderLog
import com.horionDev.climbingapp.domain.model.entities.RouteLog
import com.horionDev.climbingapp.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileViewModel(private val savedStateHandle: SavedStateHandle, private val users: Users) :
    ViewModel() {

    val galleryPopup = savedStateHandle.getStateFlow("popup", GalleryPopup.HID)
    val routeLogs = savedStateHandle.getStateFlow("routeLogs", emptyList<RouteLog>())
    val boulderLogs = savedStateHandle.getStateFlow("boulderLogs", emptyList<BoulderLog>())
    val user = savedStateHandle.getStateFlow("user", SessionManager.user)

    fun fetchRouteLogs() {
        viewModelScope.launch {
            when (val result = users.fetchRouteLogs(SessionManager.user.id)) {
                is ResultWrapper.Success -> {
                    savedStateHandle["routeLogs"] = result.value
                }

                is ResultWrapper.Failure -> {

                }
            }
        }
    }

    fun fetchBoulderLogs() {
        viewModelScope.launch {
            when (val result = users.fetchBoulderLogs(SessionManager.user.id)) {
                is ResultWrapper.Success -> {
                    savedStateHandle["boulderLogs"] = result.value
                }

                is ResultWrapper.Failure -> {

                }
            }
        }
    }

    fun updateUser(userDto: UserDto) {
        viewModelScope.launch {
            when (val result = users.updateUser(userDto)) {
                is ResultWrapper.Success -> {
                    savedStateHandle["user"] = result.value
                }

                is ResultWrapper.Failure -> {

                }
            }
        }
    }


    fun showPopup(url: String) {
        val state = GalleryPopup.SHOW
        state.url = url
        savedStateHandle["popup"] = state
    }

    fun hidePopup() {
        savedStateHandle["popup"] = GalleryPopup.HID
    }

    enum class GalleryPopup(var url: String) {
        HID(""), SHOW("")
    }
}
