package com.dicoding.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.entity.FavoriteUser
import com.dicoding.githubuser.data.response.DetailUserResponse
import com.dicoding.githubuser.data.retrofit.ApiConfing
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(application)
        favoriteUserViewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteUserViewModel::class.java)

        val sectionsPagerAdapter = FollowPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val dataGithub = intent.getStringExtra(GITHUB)
        val photoGithub = intent.getStringExtra(PPGITHUB)

        if (dataGithub != null) {
            findUsername(dataGithub)

            sectionsPagerAdapter.username = dataGithub
        }

        val btnFav = binding.fabFavorite

        val getProfile = favoriteUserViewModel.getFavoriteUserByUsername(dataGithub.toString())
        getProfile.observe(this) { favoriteUser ->
            if (favoriteUser != null) {
                btnFav.setImageResource(R.drawable.ic_favorite_fill)
                btnFav.setOnClickListener {
                    var favoriteUsers = FavoriteUser(dataGithub.toString(), photoGithub)
                    favoriteUserViewModel.delete(favoriteUsers)
                    Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show()
                }

            } else {
                btnFav.setImageResource(R.drawable.ic_favorite_nofill)
                btnFav.setOnClickListener {
                    var favoriteUsers = FavoriteUser(dataGithub.toString(), photoGithub)
                    favoriteUserViewModel.insert(favoriteUsers)
                    Toast.makeText(this, R.string.insert, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun findUsername(username: String) {
        showLoading(true)
        val client = ApiConfing.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        with(responseBody) {
                            setUserData(name, login, avatarUrl, followers.toString(), following.toString())
                        }
                    }
                } else {
                    Log.e(DetailActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(DetailActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserData(
            nama: String?,
            username: String?,
            photo: String?,
            followers: String,
            following: String
    ) {
        with(binding) {
            tvDetailNama.text = nama
            tvDetailUsername.text = username
            tvFollowers.text = resources.getString(R.string.followers, followers)
            tvFollowing.text = resources.getString(R.string.following, following)

        }

        Glide.with(binding.root)
            .load(photo)
            .into(binding.ivProfile)
    }

    private fun viewVisibility(visible: Boolean){
        val visibility = if (visible) View.VISIBLE else View.GONE

        with(binding) {
            ivProfile.visibility = visibility
            tvDetailNama.visibility = visibility
            tvDetailUsername.visibility = visibility
            tvFollowers.visibility = visibility
            tvFollowing.visibility = visibility
            tabs.visibility = visibility
            viewPager.visibility = visibility
            fabFavorite.visibility = visibility
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            viewVisibility(false)
        } else {
            binding.progressBar.visibility = View.GONE
            viewVisibility(true)
        }
    }

    companion object {
        const val GITHUB = "github"
        const val PPGITHUB = "photogh"
        private const val TAG = "DetaileUserActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}