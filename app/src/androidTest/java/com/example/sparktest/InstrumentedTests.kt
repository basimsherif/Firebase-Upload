package com.example.sparktest

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sparktest.util.Utils
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTests {

    @Test
    fun verifyURI() {
        val fileName = Utils.createTempFileName()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val uri = Utils.createURI(appContext, fileName)
        assert(uri.toString() == "content://com.example.sparktest.provider/cache/$fileName")
    }
}