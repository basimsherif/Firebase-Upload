package com.example.sparktest.util

import android.Manifest
import com.example.sparktest.R

/**
 * Permission class to handle run time permissions
 */
sealed class AppPermission(
    val permissionName: String, val requestCode: Int, val deniedMessageId: Int, val explanationMessageId: Int
) {
    companion object {
        val permissions: List<AppPermission> by lazy {
            listOf(
                ACCESS_CAMERA,
                READ_STORAGE,
                WRITE_STORAGE
            )
        }
        const val REQUEST_CODE_CAMERA = 42
        const val REQUEST_CODE_READSTORAGE = 43
        const val REQUEST_CODE_WRITESTORAGE = 44
    }

    object ACCESS_CAMERA : AppPermission(
        Manifest.permission.CAMERA, REQUEST_CODE_CAMERA,
        R.string.camera_permission, R.string.camera_permission
    )

    object READ_STORAGE : AppPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_READSTORAGE,
        R.string.storage_permission, R.string.storage_permission
    )

    object WRITE_STORAGE : AppPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITESTORAGE,
        R.string.storage_permission, R.string.storage_permission
    )
}