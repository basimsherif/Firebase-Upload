package com.example.sparktest.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider.getUriForFile
import java.io.File
import java.util.*


/**
 * Utility class which contains all utility methods
 */
object Utils {
    /**
     * A utility method to check if device is connected to internet
     */
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    /**
     * Util method to create a temp file name for camera upload feature
     */
    fun createTempFileName():String{
        return System.currentTimeMillis().toString() + UUID.randomUUID().toString() + ".jpg"
    }

    /**
     * A utility method to create URI to use in camera picture upload
     */
    fun createURI(context: Context, fileName:String):Uri{
        val path = File(context.externalCacheDir, "camera")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        return getUriForFile(
            context,
            context.packageName.toString() + ".provider",
            image
        )
    }
}