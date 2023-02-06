package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.repositories.AdRepository
import com.example.camperpro.data.ResultWrapper

class FetchAds(private val ads: AdRepository): IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<Ad>> {
        return ads.all()
    }
}