package com.example.sparktest.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.intent.IntentCallback
import com.example.sparktest.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


/*** Utility class which contains all utility methods
*/
object Utils {
    /**
     * This function is used to safely check if a view is visible or not.
     * @param viewInteraction view interaction need to check
     * @param viewAssert view assert need to check
     * @return will return true if its found, else return false
     */
    fun onViewCheckSafe(
        viewInteraction: ViewInteraction,
        viewAssert: ViewAssertion?
    ): Boolean {
        val checkResult = BooleanArray(1)
        checkResult[0] = true
        viewInteraction.withFailureHandler { throwable, matcher -> checkResult[0] = false }
            .check(viewAssert)
        return checkResult[0]
    }

    /**
     * Util method to save an image for gallery upload test
     */
    fun savePickedImage(activity: Activity) {
        //Create bitmap from an image
        val bm = BitmapFactory.decodeResource(activity.resources, R.drawable.placeholder)
        val dir = activity.externalCacheDir
        //Push the bitmap to cache
        val file = File(dir?.path, "pickImageResult.jpeg")
        val outStream: FileOutputStream?
        try {
            outStream = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            with(outStream) {
                flush()
                close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Util method to get stub intent for gallery pick test
     */
    fun createStubIntentForGallery(activity: Activity): Intent {
        //Get the bitmap and generate URI
        val dir = activity.externalCacheDir
        val file = File(dir?.path, "pickImageResult.jpeg")
        val uri = Uri.fromFile(file)
        val bundle = Bundle()
        val parcels: ArrayList<Parcelable> = ArrayList()
        val resultData = Intent()
        resultData.data = uri
        parcels.add(uri)
        bundle.putParcelableArrayList(Intent.EXTRA_STREAM, parcels)
        // Create the Intent that will include the bundle.
        resultData.putExtras(bundle)
        return resultData
    }

    /**
     * Intent callback for camera pick test
     */
    fun intentCallback(resourceId : Int = R.drawable.placeholder) : IntentCallback {
        return IntentCallback {
            if (it.action == MediaStore.ACTION_IMAGE_CAPTURE) {
                it.extras?.getParcelable<Uri>(MediaStore.EXTRA_OUTPUT).run {
                    val context : Context = InstrumentationRegistry.getInstrumentation().targetContext
                    val outStream = context.contentResolver.openOutputStream(this!!)
                    val bitmap : Bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                }
            }
        }
    }
}