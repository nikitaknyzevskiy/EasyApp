/*
 * Copyright (c) 2020.
 * Nikita Knyazievsky
 * Triare
 */

package com.triare.basearch.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.triare.basearch.model.UserModel

@Dao
interface UserDao {

    @Query("select * from usermodel")
    suspend fun getAllUsers(): List<UserModel>

    @Query("select * from usermodel where id = :id")
    suspend fun getUser(id: String): UserModel

    @Query("select * from usermodel where id = :id")
    fun getUserLive(id: String): LiveData<UserModel>

    @Insert(onConflict = REPLACE)
    fun save(data: List<UserModel>)

    @Insert(onConflict = REPLACE)
    fun save(data: UserModel)

}