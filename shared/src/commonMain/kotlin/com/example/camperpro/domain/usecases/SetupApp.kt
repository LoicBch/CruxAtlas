package com.example.camperpro.domain.usecases

import com.example.camperpro.data.datasources.remote.CamperProApi
import com.example.camperpro.utils.Constants.PreferencesKey.LAST_SETUP_TIMESTAMP
import com.example.camperpro.utils.Globals
import com.example.camperpro.utils.KMMCalendar
import com.example.camperpro.utils.KMMPreference
import com.jetbrains.kmm.shared.data.ResultWrapper

class SetupApp(
    private val calendar: KMMCalendar, private val preferences: KMMPreference, private
    val
    camperProApi:
    CamperProApi
) :
    IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<Nothing> {
        return when (val starter = camperProApi.starter()) {
            is ResultWrapper.Success -> {
                starter.value?.let { const ->
                    const.filterBrands.forEach {
                        addFilterPreferenceIfItDoesNotExist(preferences, it)
                    }
                    const.filterServices.forEach {
                        addFilterPreferenceIfItDoesNotExist(preferences, it)
                    }
                    Globals.menuLinks = const.menuLinks
                    preferences.put(LAST_SETUP_TIMESTAMP, calendar.getCurrentTime())
                }
                ResultWrapper.Success(null)
            }
            is ResultWrapper.Failure -> starter
        }
    }
}

fun addFilterPreferenceIfItDoesNotExist(preferences: KMMPreference, filter: String) {
    if (!preferences.contains(filter)) preferences.put(filter, false)
}

