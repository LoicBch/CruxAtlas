package com.appmobiledition.laundryfinder.data.datasources.local

import com.appmobiledition.laundryfinder.database.CamperproDatabase

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = CamperproDatabase(databaseDriverFactory.createDriver())
}