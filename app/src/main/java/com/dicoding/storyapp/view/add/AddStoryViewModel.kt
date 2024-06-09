package com.dicoding.storyapp.view.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.response.AddStoryResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: UserRepository): ViewModel() {

    private val _addStoryResult = MutableLiveData<AddStoryResponse>()
    val addStoryResult: LiveData<AddStoryResponse> = _addStoryResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun addStory(file: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.addStory(file, description, lat, lon)
                _addStoryResult.value = response
                _isSuccess.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
                _isSuccess.value = false
            }
        }
    }
}