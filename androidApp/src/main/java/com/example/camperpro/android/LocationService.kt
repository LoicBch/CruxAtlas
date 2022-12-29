package com.example.camperpro.android

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.camperpro.domain.model.Location
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.Globals
import com.example.camperpro.utils.LocationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationClient(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun start() {

        locationClient.getLocationUpdates(Constants.GPS_UPDATE_INTERVAL)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude
                val long = location.latitude
                Log.d("location", "$lat, $long")
                Globals.geoLoc.lastKnownLocation = Location(lat, long)
                if (Globals.geoLoc.lastSearchedLocation == null){
                    Globals.geoLoc.lastSearchedLocation = Location(lat, long)
                }
            }
            .launchIn(serviceScope)
    }

    private fun stop() {
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}