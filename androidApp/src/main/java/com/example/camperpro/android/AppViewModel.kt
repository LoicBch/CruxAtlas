package com.example.camperpro.android

import android.content.Context
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.android.components.getValueByKey
import com.example.camperpro.android.components.readAllKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filters")
val Context.dataStoreLastSearch: DataStore<Preferences> by preferencesDataStore(name = "lastSearch")

@OptIn(ExperimentalMaterialApi::class)
class AppViewModel : ViewModel() {

    val bottomSheetIsShowing = ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val filters = MutableStateFlow<List<Filter>>(emptyList())
    val lastFilterSearch = MutableStateFlow<Set<String>>(emptySet())

    val userClickedAroundMe = MutableStateFlow(false)

    //    call filters and save them in db
    fun saveFilters(context: Context) {
        viewModelScope.launch {
            context.dataStore.edit { filters ->
                val brand = booleanPreferencesKey("Motorhome brands")
                val services = booleanPreferencesKey("Motorhome services")
                val accessories = booleanPreferencesKey("Motorhome accessories")
                filters[brand] = false
                filters[services] = false
                filters[accessories] = false
                updateFilter(context)
            }

        }
    }

    private suspend fun updateFilter(context: Context) {
        val keys = context.readAllKeys()?.map { it.name } ?: emptyList()
        val filtersItems = mutableListOf<Filter>()
        keys.forEach {
            val value: Boolean = context.getValueByKey(booleanPreferencesKey(it)) as Boolean
            filtersItems.add(Filter(it, value))
        }
        filters.value = filtersItems
    }

    fun updateBooleanPreference(context: Context, key: String, value: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { filters ->
                val pref = booleanPreferencesKey(key)
                filters[pref] = value
            }
            val keys = context.readAllKeys()?.map { it.name } ?: emptyList()
            keys.forEach {
                val value: Boolean = context.getValueByKey(booleanPreferencesKey(it)) as Boolean
                Log.d("persistence", it)
                Log.d("persistence", value.toString())
            }
            updateFilter(context)
        }
    }

    fun applyFilters(context: Context, filters: List<Pair<String, Boolean>>) {
        viewModelScope.launch {
            filters.forEach {
                context.dataStore.edit { filters ->
                    val FILTER_KEY = booleanPreferencesKey(it.first)
                    filters[FILTER_KEY] = it.second
                }
            }
        }
    }

    fun saveSearch(context: Context, lastSearchs: Set<String>){
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                val searchKey = stringSetPreferencesKey("lastSearch")
                preferences[searchKey] = lastSearchs
            }
        }
    }

    fun getLastSearch(context: Context){
        val searchKey = stringSetPreferencesKey("lastSearch")
        viewModelScope.launch {
            context.dataStore.data.map {
                lastFilterSearch.value = it[searchKey] ?: emptySet()
            }
        }
    }

    fun getBooleanFromSharedPref(key: String, context: Context): Flow<Boolean?> =
        context.dataStore.data.map { preferences ->
            val FILTER_KEY = booleanPreferencesKey(key)
            preferences[FILTER_KEY]
        }

    data class Filter(val filterKey: String, val selected: Boolean)
}

//enum class Filters(
//    val filterKey: String, val selected: Boolean, val options: List<Pair<String, Boolean>>?
//) {
//    AllPlaces("all_places", false, null), Brands("brands", false, null), Services(
//        "services", false, null
//    ),
//    Accessories("accessories", false, null)
//}