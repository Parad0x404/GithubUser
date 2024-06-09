package com.dicoding.githubuser.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.local.FavoriteUserRepository
import com.dicoding.githubuser.data.local.entity.FavoriteUser

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUsers()

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun getFavoriteUserByUsername(favoriteUser: String): LiveData<FavoriteUser> = mFavoriteUserRepository.getFavoriteUserByUsername(favoriteUser)

}