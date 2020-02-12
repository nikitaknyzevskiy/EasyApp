/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.triare

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

object Triare {

    var BASE_URL: String? = null
    var DB_NAME: String? = null
    var APP_DATABASE: Any? = null

    fun <T : RoomDatabase> init(
        context: Context,
        apiUrl: String,
        dbName: String,
        appDatabase: Class<T>,
        vararg migrations: Migration = emptyArray()
    ) {
        BASE_URL = apiUrl
        DB_NAME = dbName
        APP_DATABASE = Room
            .databaseBuilder<T>(context, appDatabase, dbName)
            .allowMainThreadQueries()
            .addMigrations(*migrations)
    }

}