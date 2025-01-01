package com.horionDev.climbingapp.android.newsFeed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.model.entities.NewsItem
import com.horionDev.climbingapp.domain.repositories.NewRepository
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val newRepository: NewRepository
) : ViewModel() {

    val news = savedStateHandle.getStateFlow("news", emptyList<NewsItem>())
    val isLoading = savedStateHandle.getStateFlow("isLoading", false)

    private var lastPageLoaded = 1

    init {
        loadNews(lastPageLoaded)
    }

    private fun loadNews(page: Int, replaceAll: Boolean = false) {
        savedStateHandle["isLoading"] = true
        viewModelScope.launch {
            when (val result = newRepository.getNews(page)) {
                is ResultWrapper.Success -> {
                    if (replaceAll) {
                        savedStateHandle["news"] = result.value
                    } else {
                        val currentSpotsProducts =
                            savedStateHandle.get<List<NewsItem>>("news") ?: emptyList()
                        val updatedSpotsProducts = currentSpotsProducts + result.value
                        savedStateHandle["news"] = updatedSpotsProducts
                    }
                    lastPageLoaded = page
                    savedStateHandle["isLoading"] = false
                }

                is ResultWrapper.Failure -> {
                    savedStateHandle["isLoading"] = false
                }
            }
        }
    }

    fun loadMoreNews() {
        loadNews(++lastPageLoaded)
    }
}