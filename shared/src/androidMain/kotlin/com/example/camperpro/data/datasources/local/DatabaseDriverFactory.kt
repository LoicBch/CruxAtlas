package com.example.camperpro.data.datasources.local

import android.content.Context
import com.example.camperpro.database.CamperproDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver{
        return AndroidSqliteDriver(CamperproDatabase.Schema, context, "camperpro.db")
    }
}