package com.example.camperproglobal.domain.usecases

import com.jetbrains.kmm.shared.data.ResultWrapper

interface IFetchUseCase<T: Any> {

    @Throws(IllegalStateException::class)
    suspend fun execute(): ResultWrapper<T>
}

