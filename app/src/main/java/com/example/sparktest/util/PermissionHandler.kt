package com.example.sparktest.util

import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment

/**
 * Kotlin Extension methods for handling run time permissions
 */
fun Fragment.isGranted(permission: AppPermission) = run {
    context?.let {
        (PermissionChecker.checkSelfPermission(
            it, permission.permissionName
        ) == PermissionChecker.PERMISSION_GRANTED)
    } ?: false
}

fun Fragment.shouldShowRationale(permission: AppPermission) = run {
    shouldShowRequestPermissionRationale(permission.permissionName)
}

fun Fragment.requestPermission(permission: AppPermission) {
    requestPermissions(
        arrayOf(permission.permissionName), permission.requestCode
    )
}

fun Fragment.handlePermission(
    permission: AppPermission,
    onGranted: (AppPermission) -> Unit,
    onDenied: (AppPermission) -> Unit,
    onRationaleNeeded: ((AppPermission) -> Unit)? = null
) {
    when {
        isGranted(permission) -> onGranted.invoke(permission)
        shouldShowRationale(permission) -> onRationaleNeeded?.invoke(permission)
        else -> onDenied.invoke(permission)
    }
}

fun Fragment.handlePermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray,
    onPermissionGranted: (AppPermission) -> Unit,
    onPermissionDenied: ((AppPermission) -> Unit)? = null,
    onPermissionDeniedPermanently: ((AppPermission) -> Unit)? = null
) {

    AppPermission.permissions.find {
        it.requestCode == requestCode
    }?.let { appPermission ->
        val permissionGrantResult = mapPermissionsAndResults(
            permissions, grantResults
        )[appPermission.permissionName]
        when {
            PermissionChecker.PERMISSION_GRANTED == permissionGrantResult -> {
                onPermissionGranted(appPermission)
            }
            shouldShowRationale(appPermission) -> onPermissionDenied?.invoke(appPermission)
            else -> {
                onPermissionDeniedPermanently?.invoke(appPermission)
            }
        }
    }
}


private fun mapPermissionsAndResults(
    permissions: Array<out String>, grantResults: IntArray
): Map<String, Int> = permissions.mapIndexedTo(
    mutableListOf()
) { index, permission -> permission to grantResults[index] }.toMap()
