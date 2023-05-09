package com.example.mystoryapps.viewModel


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapps.api.ApiConfig
import com.example.mystoryapps.response.AllStoryResponse
import com.example.mystoryapps.response.ListStoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : ViewModel() {

    private val _mapStoryList = MutableLiveData<ArrayList<ListStoryItem>>()
    val storyList: LiveData<ArrayList<ListStoryItem>> = _mapStoryList

    companion object {
        private const val TAG = "MapViewModel"
    }

    fun getStory(token: Context) {
        val client = ApiConfig.getApiService(token).getLocation(1)
        client.enqueue(object : Callback<AllStoryResponse> {
            override fun onResponse(call: Call<AllStoryResponse>, response: Response<AllStoryResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _mapStoryList.value = response.body()?.listStory as ArrayList<ListStoryItem>?
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
