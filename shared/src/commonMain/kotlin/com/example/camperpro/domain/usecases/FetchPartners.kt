package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.repositories.Partners
import com.example.camperpro.domain.model.Partner
import com.example.camperpro.domain.repositories.PartnerRepository

class FetchPartners(private val partners: PartnerRepository) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<Partner>> {
        return partners.all()
    }
}