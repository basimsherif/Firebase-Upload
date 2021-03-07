package com.example.sparktest.util

/**
 * Wrapper class for parsing API response
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    /**
     * Status enum values
     */
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> success(): Resource<T> {
            return Resource(
                Status.SUCCESS,
                null,
                null
            )
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                message
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }
}