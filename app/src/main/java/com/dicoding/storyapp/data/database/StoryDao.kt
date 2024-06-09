package com.dicoding.storyapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.storyapp.data.api.response.ListStoryItem

@Dao
interface StoryDao {

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<ListStoryItem>)

    @Query("DELETE FROM story")
    suspend fun deleteAll()

}