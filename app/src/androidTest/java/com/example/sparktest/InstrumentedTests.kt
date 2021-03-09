package com.example.sparktest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sparktest.data.local.AppDatabase
import com.example.sparktest.data.local.ImageDao
import com.example.sparktest.data.model.Image
import com.example.sparktest.util.Utils
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTests {

    private lateinit var imageDao: ImageDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        imageDao = db.imageDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Instrumented test to verify ROOM for adding images to local DB and retrieving it
     */
    @Test
    @Throws(Exception::class)
    fun writeImageAndReadInList() = runBlocking {
        val image1 = Image().apply {
            imageName = "Test Image"
            downloadURL = "http://com.testimage.jpg"
        }
        val image2 = Image().apply {
            imageName = "Test Image2"
            downloadURL = "http://com.testimage2.jpg"
        }
        imageDao.insertAll(listOf(image1, image2))
        val imageList = imageDao.getAllImages()
        assert(imageList[0] == image1)
        assert(imageList[1] == image2)
    }

    /**
     * Test to verify if URI is generating properly
     */
    @Test
    fun verifyURI() {
        val fileName = Utils.createTempFileName()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val uri = Utils.createURI(appContext, fileName)
        assert(uri.toString() == "content://com.example.sparktest.provider/cache/$fileName")
    }
}