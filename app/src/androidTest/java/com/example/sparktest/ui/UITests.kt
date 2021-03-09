package com.example.sparktest.ui

import android.app.Activity
import android.app.Instrumentation
import android.provider.MediaStore
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.intent.IntentMonitorRegistry
import com.example.sparktest.R
import com.example.sparktest.ui.pages.HomePage
import com.example.sparktest.ui.utils.Utils
import com.example.sparktest.util.EspressoIdlingResource.idlingResource
import com.example.sparktest.view.MainActivity
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI Automation test cases to verify major UI flows
 */
@RunWith(AndroidJUnit4::class)
class UITests {

    //Test rule to enable intent stub and testing
    @get:Rule
    var mActivityTestRule = IntentsTestRule(MainActivity::class.java)

    //Permissions are handled here
    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    //POM object for home page
    private val homePage = HomePage()

    /**
     * This test case will verify if home page displayed properly
     */
    @Test
    fun verifyHomeScreenLaunched() {
        homePage.assertHomePageOpened()
    }

    /**
     * This test case will verify the bottom menu items for upload
     */
    @Test
    fun verifyUploadOptions() {
        homePage.assertHomePageOpened()
        homePage.tapUploadButton()
        homePage.assertUploadOptionsUI()
    }

    /**
     * This test case will verify camera upload by doing intent stub
     */
    @Test
    fun verifyCameraUpload() {
        //Register the idling resource
        IdlingRegistry.getInstance().register(idlingResource)
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        val cameraIntentCallback = Utils.intentCallback(R.drawable.placeholder)
        IntentMonitorRegistry.getInstance().addIntentCallback(cameraIntentCallback)
        homePage.performCameraUpload()
    }

    /**
     * This test case will verify gallery upload by doing intent stub
     */
    @Test
    fun verifyGalleryUpload() {
        //Register the idling resource
        IdlingRegistry.getInstance().register(idlingResource)
        Utils.savePickedImage(mActivityTestRule.activity)
        val imgGalleryResult = Utils.createStubIntentForGallery(mActivityTestRule.activity)
        intending(not(isInternal()))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, imgGalleryResult))
        homePage.performGalleryUpload()
    }

    // Unregister Idling Resource so it can be garbage collected and does not leak any memory
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}