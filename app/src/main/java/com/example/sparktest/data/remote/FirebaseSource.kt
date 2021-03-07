package com.example.sparktest.data.remote

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sparktest.R
import com.example.sparktest.data.model.Image
import com.example.sparktest.data.model.Image.Companion.toImage
import com.example.sparktest.util.Resource
import com.example.sparktest.util.Utils
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * Firebase source class used to communicate with Firebase and fetch data
 */
class FirebaseSource @Inject constructor(
    private val applicationContext: Context
) {
    private val storage = Firebase.storage
    private val listRef = storage.reference.child("images")
    private val TAG = "Firebase"
    private val uploadLiveData: MutableLiveData<Resource<Any>> = MutableLiveData()

    /**
     * A LiveData listens to upload events
     */
    fun getUploadLiveData(): MutableLiveData<Resource<Any>> {
        return uploadLiveData
    }

    fun getImages(): Flow<List<Image>> {
        return callbackFlow {
            //Check if network available
            if (!Utils.isNetworkAvailable(applicationContext)) {
                cancel(
                    message = applicationContext.getString(R.string.error_network)
                )
                return@callbackFlow
            }
            listRef.listAll().addOnFailureListener {
                cancel(
                    message = applicationContext.getString(R.string.error_firebase),
                    cause = it
                )
                return@addOnFailureListener
            }
                .addOnSuccessListener { listResult ->
                    listResult.items.forEach { storageRef ->
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val map = listResult.items
                                .mapNotNull {
                                    //Convert StorageReference to Image object
                                    var imageItem = storageRef.toImage()
                                    //Get download URL
                                    imageItem?.downloadURL = uri.toString()
                                    imageItem
                                }
                            offer(map)
                        }.addOnFailureListener {
                            cancel(
                                message = applicationContext.getString(R.string.error_firebase),
                                cause = it
                            )
                            return@addOnFailureListener
                        }
                    }
                }
            awaitClose {
                Log.d(TAG, "Cancelling images listener")
            }
        }
    }

    fun upload(uri: Uri) {
        //Check if network available
        if (!Utils.isNetworkAvailable(applicationContext)) {
            uploadLiveData.value =
                Resource.error(applicationContext.getString(R.string.error_network))
            return
        }
        //Notify that we are starting upload process
        uploadLiveData.value = Resource.loading()
        val storageRef = storage.reference
        val fileRef = storageRef.child("images/" + UUID.randomUUID().toString())
        var uploadTask = fileRef.putFile(uri)
        //Handle success/failed upload process
        uploadTask.addOnFailureListener {
            uploadLiveData.value = Resource.error(it.message!!)
        }.addOnSuccessListener {
            uploadLiveData.value = Resource.success()
        }
    }
}