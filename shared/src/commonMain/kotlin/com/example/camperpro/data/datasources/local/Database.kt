package com.example.camperpro.data.datasources.local

import com.example.camperpro.database.CamperproDatabase

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = CamperproDatabase(databaseDriverFactory.createDriver())
}