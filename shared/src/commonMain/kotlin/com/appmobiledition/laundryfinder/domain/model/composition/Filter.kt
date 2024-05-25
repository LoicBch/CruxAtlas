package com.appmobiledition.laundryfinder.domain.model.composition

import com.appmobiledition.laundryfinder.utils.FilterType
import com.appmobiledition.laundryfinder.utils.Globals

class Filter(
    val category: FilterType = FilterType.UNSELECTED_DEALER,
    val filterId: String = "",
    val isSelected: Boolean = false
)

val Filter.filterName
    get() = when (category) {
        FilterType.SERVICE -> Globals.filters.services.find { it.first == filterId }?.second ?: ""
        FilterType.BRAND -> Globals.filters.brands.find { it.first == filterId }?.second ?: ""
        else -> {
            ""
        }
    }

fun Filter.getIdFromFilterName(name: String) = when (category) {
    FilterType.SERVICE -> Globals.filters.services.find { it.second == name }?.first
    FilterType.BRAND -> Globals.filters.brands.find { it.second == name }?.first
    else -> {
        ""
    }
}