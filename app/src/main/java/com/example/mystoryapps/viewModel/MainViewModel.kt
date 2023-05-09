package com.example.mystoryapps.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapps.api.ApiConfig
import com.example.mystoryapps.data.StoryRepository
import com.example.mystoryapps.response.AllStoryResponse
import com.example.mystoryapps.response.ListStoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> = storyRepository.getStory().cachedIn(viewModelScope)

}

class MainViewFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val apiService = ApiConfig.getApiService(context)
            val storyRepository = StoryRepository(apiService)
            return MainViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}