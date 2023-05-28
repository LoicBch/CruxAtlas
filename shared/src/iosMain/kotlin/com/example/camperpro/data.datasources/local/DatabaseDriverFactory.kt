package com.example.camperpro.data.datasources.local

import com.example.camperpro.database.CamperproDatabase
import com.example.camperpro.utils.KMMContext
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(CamperproDatabase.Schema, "camperpro.db")
    }
}