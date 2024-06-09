package com.dicoding.storyapp.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.response.DetailStoryResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _story = MutableLiveData<DetailStoryResponse>()
    val story: LiveData<DetailStoryResponse> = _story

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoryDetail(storyId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getStoryDetails(storyId)
                _story.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}