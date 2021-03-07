package com.example.sparktest.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.sparktest.data.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

/**
 * Data access strategy to fetch images from Firebase and cache it to DB
 */
fun <T> performFirebaseSync(
    databaseQuery: suspend () -> LiveData<T>,
    firebaseFetch: suspend () -> Flow<List<Image>>,
    saveCallResult: suspend (List<Image>) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        //Will notify to display the progressbar
        emit(Resource.loading())

        //Return data from local DB if available
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)

        //Fetch from Firebase and update DB
        firebaseFetch.invoke().catch { e ->
            //Error handling
            emit(Resource.error(e.message!!))
            emitSource(source)
        }.collect {
            //Save data to DB once fetch is completed
            saveCallResult(it)
        }
    }