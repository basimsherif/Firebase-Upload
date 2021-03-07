package com.example.sparktest.util

import android.Manifest
import com.example.sparktest.R

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
    }

    object ACCESS_CAMERA : AppPermission(
        Manifest.permission.CAMERA, 42,
        R.string.camera_permission, R.string.camera_permission
    )

    object READ_STORAGE : AppPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE, 43,
        R.string.storage_permission, R.string.storage_permission
    )

    object WRITE_STORAGE : AppPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, 44,
        R.string.storage_permission, R.string.storage_permission
    )
}