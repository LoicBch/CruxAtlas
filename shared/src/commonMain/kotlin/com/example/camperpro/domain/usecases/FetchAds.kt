package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.repositories.AdRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchAds(private val ads: AdRepository) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<Ad>> {
        return ads.all()
    }
}

class RFetchAds : KoinComponent {
    private val fetchAds: FetchAds by inject()
    suspend fun execute(): List<Ad>? =
        fetchAds.invoke().flattenIos()
}