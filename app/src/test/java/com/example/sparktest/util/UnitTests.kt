package com.example.sparktest.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Local unit test, which will execute on the development machine (host).
 */
class UnitTests {

    /**
     * Unit test to verify image filname is generating properly
     */
    @Test
    fun `return a valid filename`() {
        val fileName = Utils.createTempFileName()
        assertThat(fileName.substring(fileName.lastIndexOf(".")) == ".jpg").isTrue()
    }
}