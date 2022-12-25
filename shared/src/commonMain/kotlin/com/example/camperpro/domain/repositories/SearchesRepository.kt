package com.example.camperpro.domain.repositories

import com.example.camperpro.domain.model.Search

interface SearchesRepository {
    suspend fun delete(label: String)
    suspend fun allOfCategory(category: String): List<Search>?
    suspend fun add(search: Search)
}