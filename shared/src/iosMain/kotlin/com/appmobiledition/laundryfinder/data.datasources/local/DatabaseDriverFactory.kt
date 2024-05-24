package com.appmobiledition.laundryfinder.data.datasources.local

import com.appmobiledition.laundryfinder.database.CamperproDatabase
import com.appmobiledition.laundryfinder.utils.KMMContext
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(CamperproDatabase.Schema, "camperpro.db")
    }
}