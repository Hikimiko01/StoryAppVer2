package com.dicoding.storyapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.response.LoginResponse
import com.dicoding.storyapp.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String, callback: (LoginResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                withContext(Dispatchers.Main) {
                    callback(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(LoginResponse(error = true, message = e.localizedMessage))
                }
            }
        }
    }
}