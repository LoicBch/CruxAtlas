package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.model.News
import com.example.camperpro.domain.repositories.NewsRepository
import com.example.camperpro.data.ResultWrapper

class FetchNews(private val allNews: NewsRepository) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<News>> {
        return allNews.all()
    }
}