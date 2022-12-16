package com.example.camperpro.android.onBoarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.domain.usecases.SetupApp
import com.jetbrains.kmm.shared.data.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val setupApp: SetupApp) : ViewModel() {

    init {
        initApp()
    }

    var setupIsComplete =  false

    private fun initApp() {
        viewModelScope.launch {
            when (val call = setupApp()) {
                is ResultWrapper.Failure -> {
                    Log.d("TAG", call.throwable.toString())
                }
                is ResultWrapper.Success -> {
                    setupIsComplete = true
                }
            }
        }
    }
}