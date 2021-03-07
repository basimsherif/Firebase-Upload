package com.example.sparktest.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sparktest.data.model.Image


/**
 * DAO class for database operations for images
 */
@Dao
interface ImageDao {

    @Query("SELECT * FROM image_table")
    fun getAllImagesLiveData(): LiveData<List<Image>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(imageList: List<Image>)
}