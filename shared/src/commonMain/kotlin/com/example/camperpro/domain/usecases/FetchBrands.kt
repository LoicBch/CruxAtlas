package com.example.camperpro.domain.usecases

import com.example.camperpro.domain.repositories.BrandRepository
import com.jetbrains.kmm.shared.data.ResultWrapper
import com.example.camperpro.domain.model.Location

class FetchBrands(private val brands: BrandRepository): IBaseUsecase {
    suspend operator fun invoke(location: Location): ResultWrapper<List<String>> {
        return brands.all()
    }
}