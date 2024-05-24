package com.appmobiledition.laundryfinder.android.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appmobiledition.laundryfinder.data.ResultWrapper
import com.appmobiledition.laundryfinder.domain.usecases.SetupApp
import com.appmobiledition.laundryfinder.utils.Globals
import com.appmobiledition.laundryfinder.utils.LanguageManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val setupApp: SetupApp,
    private val languageManager: LanguageManager
) : ViewModel() {

    var gpsLocationIsAsked = MutableStateFlow(false)
    private val globalsVarsAreSet = MutableStateFlow(false)

    var setupIsComplete = combine(
        gpsLocationIsAsked, globalsVarsAreSet
    ) { gpsLocationIsAsked, globalVarsAreSet ->
        gpsLocationIsAsked && globalVarsAreSet
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    fun initApp() {
        setDeviceConstants()
        viewModelScope.launch {
            when (setupApp.invoke()) {
                is ResultWrapper.Failure -> {
                }

                is ResultWrapper.Success -> {
                    globalsVarsAreSet.update { true }
                }
            }
        }
    }

    fun onUserRespondToLocationPermission(allowing: Boolean) {
        gpsLocationIsAsked.update { allowing }
    }

    private fun setDeviceConstants() {
        Globals.geoLoc.appLanguage = "FR"
        Globals.geoLoc.deviceLanguage = languageManager.getDeviceLanguage()
        Globals.geoLoc.deviceCountry = languageManager.getDeviceCountry()
    }

}