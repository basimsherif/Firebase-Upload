package com.example.sparktest.data.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sparktest.data.local.LocalDataSource
import com.example.sparktest.data.model.Image
import com.example.sparktest.data.remote.FirebaseSource
import com.example.sparktest.util.Resource
import com.example.sparktest.util.performFirebaseSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Repository class used to hold all firebase/localdb operation
 * @param localDataSource local data source
 * @param firebaseSource Firebase data source
 */
class Repository @Inject constructor(
    private val firebaseSource: FirebaseSource,
    private val localDataSource: LocalDataSource
) {

    /**
     * A LiveData listens to upload events
     */
    fun getUploadLiveData(): MutableLiveData<Resource<Any>> {
        return firebaseSource.getUploadLiveData()
    }

    /**
     * Method to fetch all images from Firebase and cache it to local DB
     */
    fun fetchAllImages() = performFirebaseSync(
        databaseQuery = { localDataSource.getAllImages() },
        firebaseFetch = { fetchFromFirebase() },
        saveCallResult = { saveDataToLocalDB(it) }
    )

    /**
     * Method to upload image to Firebase
     */
    fun uploadToFirebase(uri: Uri) {
        firebaseSource.upload(uri)
    }

    /**
     * Method to fetch data from Firebase
     */
    private fun fetchFromFirebase(): Flow<List<Image>> {
        return firebaseSource.getImages()
    }

    /**
     * Method to save data to local DB after fetching from Firebase
     */
    private suspend fun saveDataToLocalDB(imageList: List<Image>) {
        localDataSource.insertAllImages(imageList)
    }

    /**
     * Function to refresh gallery by syncing with Firebase
     */
    suspend fun refreshImages() {
        fetchFromFirebase().collect { saveDataToLocalDB(it) }
    }

}