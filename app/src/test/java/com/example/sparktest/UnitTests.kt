package com.example.sparktest

import com.example.sparktest.util.Utils
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Local unit test, which will execute on the development machine (host).
 */
class UnitTests {
    @Test
    fun `return a valid filename`() {
        val fileName = Utils.createTempFileName()
        assertThat(fileName.substring(fileName.lastIndexOf(".")) == ".jpg").isTrue()
    }
}