package com.example.camperpro.managers.location.observers

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.camperpro.managers.location.LocationManager
import com.example.camperpro.managers.location.extension.activity
import com.example.camperpro.managers.network.extension.activity

internal object ActivityLifecycleObserver : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity) {}
    override fun onActivityStarted(p0: Activity) {}
    override fun onActivityDestroyed(p0: Activity) {}
    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
    override fun onActivityStopped(p0: Activity) {}
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        val currentActivity = LocationManager.activity
        if (currentActivity != null && !currentActivity.isFinishing && !currentActivity.isDestroyed) {
            return
        }
        LocationManager.activity = p0
    }

    override fun onActivityResumed(p0: Activity) {}
}