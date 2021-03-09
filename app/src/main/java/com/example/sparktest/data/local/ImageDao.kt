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

    /**
     * Get all images data from local DB and return as LiveData
     */
    @Query("SELECT * FROM image_table")
    fun getAllImagesLiveData(): LiveData<List<Image>>

    /**
     * Get all images data from local DB
     */
    @Query("SELECT * FROM image_table")
    fun getAllImages(): List<Image>

    /**
     * Insert list of images to local DB
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(imageList: List<Image>)
}