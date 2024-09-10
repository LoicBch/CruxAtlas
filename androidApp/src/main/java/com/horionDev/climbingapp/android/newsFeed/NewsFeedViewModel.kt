package com.horionDev.climbingapp.android.newsFeed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.model.NewsItem
import com.horionDev.climbingapp.domain.model.Place
import com.horionDev.climbingapp.domain.repositories.NewRepository
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val newRepository: NewRepository
): ViewModel(){

    val news = savedStateHandle.getStateFlow("news", emptyList<NewsItem>())

    init {
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            when (val result = newRepository.getNews(0)) {
                is ResultWrapper.Success -> savedStateHandle["news"] = result.value
                is ResultWrapper.Failure -> Unit
            }
        }
    }
}