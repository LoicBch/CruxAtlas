package com.example.camperpro.android.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.domain.usecases.SetupApp
import com.jetbrains.kmm.shared.data.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val setupApp: SetupApp) : ViewModel() {

    private val _setupIsComplete = MutableStateFlow(false)
    var setupIsComplete: StateFlow<Boolean> = _setupIsComplete



    fun initApp() {
        viewModelScope.launch {
            when (val call = setupApp.invoke()) {
                is ResultWrapper.Failure -> {
                }

                is ResultWrapper.Success -> {
                    _setupIsComplete.update { true }
                }
            }
        }
    }
}