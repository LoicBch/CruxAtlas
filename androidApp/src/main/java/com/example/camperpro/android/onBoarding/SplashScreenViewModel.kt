package com.example.camperpro.android.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.usecases.SetupApp
import com.example.camperpro.utils.Globals
import com.example.camperpro.utils.LanguageManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val setupApp: SetupApp,
    private val languageManager: LanguageManager
) : ViewModel() {

    var gpsLocationIsAsked = MutableStateFlow(false)

    // TODO: reutiliser ce setup si besoi nde check plusieurs trucs
    //    private val globalsVarsAreSet = MutableStateFlow(false)
    //
    //    var setupIsComplete = combine(
    //        gpsLocationIsAsked, globalsVarsAreSet
    //    ) { gpsLocationIsAsked, globalVarsAreSet ->
    //        gpsLocationIsAsked && globalVarsAreSet
    //    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    fun initApp() {
        setDeviceConstants()
        viewModelScope.launch {
            when (setupApp.invoke()) {
                is ResultWrapper.Failure -> {
                }

                is ResultWrapper.Success -> {
//                    globalsVarsAreSet.update { true }
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