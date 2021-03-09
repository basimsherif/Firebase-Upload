package com.example.sparktest.view

import android.content.Context
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparktest.data.model.Image
import com.example.sparktest.data.repository.Repository
import com.example.sparktest.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

/**
 * View model class for Home Fragment
 */
class HomeViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository
) : ViewModel() {
    val imageAdapter: ImageAdapter = ImageAdapter()
    private var imagesData:LiveData<Resource<List<Image>>> = MutableLiveData()

    fun getUploadLiveData(): MutableLiveData<Resource<Any>> {
        return repository.getUploadLiveData()
    }

    fun getSyncLiveData(): LiveData<Resource<List<Image>>> {
        return imagesData
    }

    /**
     * Upload image to firebase
     */
    fun uploadToFirebase(uri: Uri){
        repository.uploadToFirebase(uri)
    }

    /**
     * Sync images with Firebase during startup
     */
    fun synImages(){
        imagesData = repository.fetchAllImages()
    }

    /**
     * Refresh image list when upload completes
     */
    fun refreshImages(){
        viewModelScope.launch {
            repository.refreshImages()
        }
    }

}