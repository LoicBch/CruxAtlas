package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.flattenIos
import com.example.camperpro.domain.model.Partner
import com.example.camperpro.domain.repositories.PartnerRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchPartners(private val partners: PartnerRepository) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<Partner>> {
        return partners.all()
    }
}

class RFetchPartners : KoinComponent {
    private val fetchPartners: FetchPartners by inject()
    suspend fun execute(): List<Partner>? =
        fetchPartners.invoke().flattenIos()
}