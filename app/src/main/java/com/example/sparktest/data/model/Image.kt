package com.example.sparktest.data.model

import android.os.Parcelable
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.storage.StorageReference
import kotlinx.android.parcel.Parcelize

/**
 * Data class for Recipe
 */
@Entity(tableName = "image_table")
@Parcelize
data class Image (
    @PrimaryKey
    var imageName : String = "",
    var downloadURL : String = ""
): Parcelable {
    companion object {
        fun StorageReference.toImage(): Image? {
            return try {
                Image().apply {
                    imageName = name
                    downloadURL = path
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error converting storage reference", e)
                null
            }
        }
        private const val TAG = "Image"
    }
}