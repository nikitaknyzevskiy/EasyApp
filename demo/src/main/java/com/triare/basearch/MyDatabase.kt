/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch

import androidx.room.Database
import androidx.room.RoomDatabase
import com.triare.basearch.dao.UserDao
import com.triare.basearch.model.UserModel

@Database(entities = [UserModel::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}