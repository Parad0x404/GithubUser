package com.dicoding.githubuser.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.data.retrofit.ApiConfing
import com.dicoding.githubuser.databinding.FragmentFollowsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowsFragment() : Fragment() {

    private lateinit var binding: FragmentFollowsBinding

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
        private const val TAG = "FollowsFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowsBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollows.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollows.addItemDecoration(itemDecoration)

        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)
        if (username != null) {
            if (position == 1){
                findFollows(username, 1)
            } else {
                findFollows(username, 2)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun findFollows(username: String, position: Int) {
        showLoading(true)
        val client = if (position == 1) {
            ApiConfing.getApiService().getFollowers(username)
        } else {
            ApiConfing.getApiService().getFollowing(username)
        }
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setFollowData(responseBody)
                    }
                } else {
                    Log.e(FollowsFragment.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                showLoading(false)
                Log.e(FollowsFragment.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setFollowData(items: List<ItemsItem?>?) {
        val adapter = UserAdapter()
        adapter.submitList(items)
        binding.rvFollows.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}