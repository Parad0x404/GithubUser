package com.dicoding.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.githubuser.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {

    @Query("SELECT * FROM FavoriteUser ORDER BY username ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteUser(user: FavoriteUser)

    @Delete
    fun deleteFavoriteUser(user: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>
}