package com.example.sparktest.util

/**
 * Contains a static reference IdlingResource, and should be available only in a mock build type.
 */
object EspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"
    private val mCountingIdlingResource: APIIdlingResource =
        APIIdlingResource(RESOURCE)

    fun increment() {
        mCountingIdlingResource.increment()
    }

    fun decrement() {
        mCountingIdlingResource.decrement()
    }

    val idlingResource: APIIdlingResource
        get() = mCountingIdlingResource
}