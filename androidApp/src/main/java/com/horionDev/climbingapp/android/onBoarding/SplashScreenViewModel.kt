package com.horionDev.climbingapp.android.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.repositories.UserRepository
import com.horionDev.climbingapp.utils.Constants
import com.horionDev.climbingapp.utils.Globals
import com.horionDev.climbingapp.utils.KMMPreference
import com.horionDev.climbingapp.utils.SessionManager.user
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val kmmPreference: KMMPreference,
    private val users: UserRepository,
//    private val setupApp: SetupApp,
//    private val languageManager: LanguageManager
) : ViewModel() {

    var gpsPermissionInitialized = MutableStateFlow(false)
    private val globalsVarsAreSet = MutableStateFlow(false)
    private val sessionInitialized = MutableStateFlow(false)

    var setupIsComplete = combine(
        sessionInitialized, gpsPermissionInitialized
    ) { sessionInitialized, gpsLocationIsAnswered ->
        sessionInitialized && gpsLocationIsAnswered
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    fun initApp() {
        setDeviceConstants()
        initializeUser()
        viewModelScope.launch {
            globalsVarsAreSet.update { true }
//            when (setupApp.invoke()) {
//                is ResultWrapper.Failure -> {
//                }
//
//                is ResultWrapper.Success -> {
//                    globalsVarsAreSet.update { true }
//                }
//            }
        }
    }

    private fun initializeUser() {
        val sessionToken = kmmPreference.getString(Constants.Preferences.SESSION_TOKEN)
        if (sessionToken != null) {
            viewModelScope.launch {
                when (val result = users.authenticate(sessionToken)) {
                    is ResultWrapper.Success -> {
                        user = result.value
//                        val favorites = fetchFavoriteUseCase(user.id.toString())
//                        val visited = fetchVisitedUseCase(user.id.toString())
//                        if (favorites is ResultWrapper.Success) {
//                            user.favorites = favorites.value.toMutableList()
//                        }
//                        if (visited is ResultWrapper.Success) {
//                            user.visited = visited.value.toMutableList()
//                        }
                        sessionInitialized.update { true }
                    }

                    is ResultWrapper.Failure -> {
                        kmmPreference.remove(Constants.Preferences.SESSION_TOKEN)
                        sessionInitialized.update { true }
                    }
                }
            }
        } else {
            sessionInitialized.update { true }
        }
    }

    fun onUserRespondToLocationPermission(allowing: Boolean) {
        gpsPermissionInitialized.update { allowing }
    }

    private fun setDeviceConstants() {
        Globals.GeoLoc.appLanguage = "FR"
//        Globals.geoLoc.deviceLanguage = languageManager.getDeviceLanguage()
//        Globals.geoLoc.deviceCountry = languageManager.getDeviceCountry()
    }

}