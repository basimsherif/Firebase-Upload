package com.example.sparktest.data.local

import androidx.lifecycle.LiveData
import com.example.sparktest.data.model.Image
import javax.inject.Inject

/**
 * Firebase source class used to communicate with local DB
 */
class LocalDataSource @Inject constructor(
    private val imageDao: ImageDao
) {
    /**
     * Insert a list of Images to local DB
     */
    suspend fun insertAllImages(imageList:List<Image>){
        imageDao.insertAll(imageList)
    }

    /**
     * Get the images from local DB as LiveData
     */
    fun getAllImages():LiveData<List<Image>>{
        return imageDao.getAllImagesLiveData()
    }
}