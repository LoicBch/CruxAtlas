package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.repositories.ServiceRepository
import com.jetbrains.kmm.shared.data.ResultWrapper

class FetchServices(private val services: ServiceRepository): IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<String>> {
        return services.all()
    }
}