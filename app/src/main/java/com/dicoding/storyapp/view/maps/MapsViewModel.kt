package com.dicoding.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.response.GetStoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    private val _stories = MutableLiveData<GetStoryResponse>()
    val stories: LiveData<GetStoryResponse> = _stories

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            val response = repository.getStoryWithLocation()
            _stories.postValue(response)
        }
    }
}