package com.example.camperpro.data.repositories

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.model.CheckList
import com.example.camperpro.domain.repositories.CheckListRepository

class CheckLists(private var camperProApi: Api) : CheckListRepository {
    override suspend fun all(): ResultWrapper<List<CheckList>> {
        return camperProApi.getChecklists()
    }
}