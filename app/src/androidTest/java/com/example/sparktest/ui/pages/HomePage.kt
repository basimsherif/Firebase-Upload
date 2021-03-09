package com.example.sparktest.ui.pages

import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.sparktest.R
import com.example.sparktest.ui.utils.Utils
import org.junit.Assert


/**
 * This class contains all the functions related to Home page and maintains a Page Object Model
 */
class HomePage() {
    var TAG = "Home page"

    /**
     * This function is used to verify if home page is opened
     */
    private fun ifHomePageOpened(): Boolean {
        return Utils.onViewCheckSafe(
            onView(withId(R.id.imagesRecyclerView)),
            matches(isDisplayed())
        )
    }

    /**
     * This function is used to assert home page is opened
     */
    fun assertHomePageOpened() {
        Log.d(TAG, "Verify if home page is opened")
        Assert.assertTrue("Home page not opened", ifHomePageOpened())
    }

    /**
     * This function is used to tap upload button
     */
    fun tapUploadButton() {
        Log.d(TAG, "Taping upload button")
        onView(withId(R.id.floatingActionButton)).perform(click())
    }

    /**
     * This function is used to verify upload options UI
     */
    fun assertUploadOptionsUI() {
        Log.d(TAG, "Verify upload options")
        onView(withId(R.id.upload_camera))
            .check(matches(withText(R.string.upload_from_camera)))
        onView(withId(R.id.upload_gallery))
            .check(matches(withText(R.string.upload_from_gallery)))
        onView(withId(R.id.txt_cancel))
            .check(matches(withText(R.string.cancel)))
    }

    /**
     * This function is used to tap upload from camera button
     */
    fun tapUploadFromCameraButton() {
        Log.d(TAG, "Taping upload from camera button")
        onView(withId(R.id.upload_camera)).perform(click())
    }

    /**
     * This function is used to tap upload from gallery button
     */
    fun tapUploadFromGalleryButton() {
        Log.d(TAG, "Taping upload from gallery button")
        onView(withId(R.id.upload_gallery)).perform(click())
    }

    /**
     * This function is used to perform gallery upload
     */
    fun performGalleryUpload(){
        tapUploadButton()
        tapUploadFromGalleryButton()
        Log.d(TAG, "Taping CROP button")
        onView(withText("CROP")).perform(click())
    }

    /**
     * This function is used to perform camera upload
     */
    fun performCameraUpload(){
        tapUploadButton()
        tapUploadFromCameraButton()
        Log.d(TAG, "Taping CROP button")
        onView(withText("CROP")).perform(click())
    }
}