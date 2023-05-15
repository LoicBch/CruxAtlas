package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.News
import com.example.camperpro.domain.repositories.NewsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchNews(private val allNews: NewsRepository) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<News>> {
        return allNews.all()
    }
}

class RFetchNews : KoinComponent {
    private val fetchNews: FetchNews by inject()
    suspend fun execute(): List<News>? =
        fetchNews.invoke().flattenIos()
}