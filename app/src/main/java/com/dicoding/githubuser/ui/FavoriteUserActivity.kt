package com.dicoding.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.FavoriteUserRepository
import com.dicoding.githubuser.data.local.room.FavoriteUserDatabase
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.databinding.ActivityFavoriteUserBinding

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(application)
        favoriteUserViewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteUserViewModel::class.java)

        showLoading(true)
        observeFavoriteUsers()
    }

    private fun observeFavoriteUsers() {
        favoriteUserViewModel.getAllFavoriteUsers().observe(this) { users ->
            if (users.isEmpty()) {
                showLoading(false)
                binding.tvEmptyFavorite.visibility = View.VISIBLE
                binding.rvFavorite.visibility = View.GONE
            } else {
                val items = arrayListOf<ItemsItem>()
                users.map {
                    val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                    items.add(item)
                }
                setDataUser(items)
                showLoading(false)
            }
        }
    }

    private fun setDataUser(items: ArrayList<ItemsItem>) {
        val rvFavorite = binding.rvFavorite
        val adapter = UserAdapter()
        rvFavorite.layoutManager = LinearLayoutManager(this)
        rvFavorite.adapter = adapter
        adapter.submitList(items)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}