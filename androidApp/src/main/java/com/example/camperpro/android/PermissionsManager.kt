package com.example.camperpro.android

import android.Manifest

enum class AppPermissions(identifier: String) {
    FineLocation(Manifest.permission.ACCESS_FINE_LOCATION), CoarseLocation(Manifest.permission.ACCESS_COARSE_LOCATION), BackgroundLocation(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}

class PermissionsManager {

}