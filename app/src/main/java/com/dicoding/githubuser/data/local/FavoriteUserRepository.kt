package com.dicoding.githubuser.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.local.entity.FavoriteUser
import com.dicoding.githubuser.data.local.room.FavoriteUserDao
import com.dicoding.githubuser.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.FavoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()

    fun insert(user: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insertFavoriteUser(user) }
    }

    fun delete(user: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.deleteFavoriteUser(user) }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = mFavoriteUserDao.getFavoriteUserByUsername(username)
}