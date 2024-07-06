package com.horionDev.climbingapp.data.datasources.local

import com.horionDev.climbingapp.database.Database

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory.createDriver())
}