/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch

import android.app.Application
import com.triare.triare.Triare

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Triare.init(
            context = this,
            apiUrl = "https://reqres.in/",
            dbName = "triare_demo_db",
            appDatabase = MyDatabase::class.java
        )
    }

}