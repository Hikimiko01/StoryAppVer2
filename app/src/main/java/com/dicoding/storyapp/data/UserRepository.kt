package com.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.api.response.AddStoryResponse
import com.dicoding.storyapp.data.api.response.DetailStoryResponse
import com.dicoding.storyapp.data.api.response.GetStoryResponse
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.data.api.response.LoginResponse
import com.dicoding.storyapp.data.api.response.RegisterResponse
import com.dicoding.storyapp.data.api.services.ApiService
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.mediator.StoryRemoteMediator
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDb: StoryDatabase
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStories() : LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(
                storyDb,
                apiService
            ),
            pagingSourceFactory = {
                storyDb.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun addStory(file: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?): AddStoryResponse {
        return apiService.addStory(file, description, lat, lon)
    }

    suspend fun getStoryDetails(id: String): DetailStoryResponse {
        return apiService.detailStory(id)
    }

    suspend fun getStoryWithLocation(): GetStoryResponse {
        return apiService.getStoriesWithLocation(location = 1)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference, storyDb: StoryDatabase) =
            UserRepository(apiService, userPreference, storyDb)

    }
}