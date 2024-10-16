package com.horionDev.climbingapp.android.profile

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.dto.UserDto
import com.horionDev.climbingapp.data.repositories.Users
import com.horionDev.climbingapp.domain.model.entities.Boulder
import com.horionDev.climbingapp.domain.model.entities.BoulderLog
import com.horionDev.climbingapp.domain.model.entities.Route
import com.horionDev.climbingapp.domain.model.entities.RouteLog
import com.horionDev.climbingapp.domain.model.entities.RouteWithLog
import com.horionDev.climbingapp.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import toDto

class ProfileViewModel(private val savedStateHandle: SavedStateHandle, private val users: Users) :
    ViewModel() {

    val galleryPopup = savedStateHandle.getStateFlow("popup", ProfilePopup.HID)

    //    private val _routeLogs = MutableStateFlow<List<RouteWithLog>>(emptyList())
//    val routeLogs: StateFlow<List<RouteWithLog>> = _routeLogs
    val routeLogs = savedStateHandle.getStateFlow("routeLogs", emptyList<RouteWithLog>())

    //    val boulderLogs =
//        savedStateHandle.getStateFlow("boulderLogs", emptyList<Pair<BoulderLog, Boulder>>())
    val user = savedStateHandle.getStateFlow("user", SessionManager.user)

    init {
        fetchRouteLogs()
        fetchBoulderLogs()
    }

    private fun fetchRouteLogs() {
        viewModelScope.launch {
            when (val result = users.fetchRouteLogs(SessionManager.user.id)) {
                is ResultWrapper.Success -> {
//                    _routeLogs.value = result.value
                    savedStateHandle["routeLogs"] =
                        result.value.map { RouteWithLog(it.first, it.second.toDto()) }
                }

                is ResultWrapper.Failure -> {

                }
            }
        }
    }

    private fun fetchBoulderLogs() {
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


    fun showGalleryPopup(url: String) {
        val state = ProfilePopup.GALLERY
        state.url = url
        savedStateHandle["popup"] = state
    }

    fun showUpdatePopup() {
        savedStateHandle["popup"] = ProfilePopup.UPDATE
    }

    fun hidePopup() {
        savedStateHandle["popup"] = ProfilePopup.HID
    }

    fun updatePhoto(it: ByteArray) {

        viewModelScope.launch {
            when (val result = users.updatePhoto(SessionManager.user.id, it)) {
                is ResultWrapper.Success -> {
                    savedStateHandle["user"] = result.value
                }

                is ResultWrapper.Failure -> {

                }
            }
        }
    }

    enum class ProfilePopup(var url: String) {
        HID(""), GALLERY(""), UPDATE("")
    }
}
