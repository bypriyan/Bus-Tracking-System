package com.bypriyan.bustrackingsystem.driverActivity

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.renderscript.RenderScript
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.bumptech.glide.Priority
import com.bypriyan.bustrackingsystem.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.greenrobot.eventbus.EventBus
import java.util.Objects

/*
class LocationService : Service() {

    companion object{
        const val CHANNEL_ID = "1234"
        const val NOTIFICATION_ID = 1234
    }

    private var fusedLocationProviderClient:FusedLocationProviderClient? = null
    private var locationCallback:LocationCallback?= null
    private var locationRequest:LocationRequest?= null
    private var notificationManager:NotificationManager?= null
    private var location:Location? = null

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,1000).setIntervalMillis(500).build()
        locationCallback = object :LocationCallback(){
            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult)
            }

        }

        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, "location", NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(notificationChannel)
        }

    }

    @Suppress("MissingPermission")
    fun createLocationRequest(){
        try{
            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest!!,locationCallback!!, null)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun removeLocationUpdates(){
        locationCallback?.let {
            fusedLocationProviderClient?.removeLocationUpdates(it)
        }
        stopForeground(true)
        stopSelf()
    }

    private fun onNewLocation(locationResult: LocationResult) {
        location = locationResult.lastLocation
        EventBus.getDefault().post(LocationEvent(
            latitude = location?.latitude,
            longitude = location?.longitude
        ))
        startForeground(NOTIFICATION_ID, getNotification())
    }

    fun getNotification():Notification{
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Updates")
            .setContentText("Latitude -> ${location?.latitude}\n Longitude -> ${location?.longitude}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)

        return notification.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        createLocationRequest()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        removeLocationUpdates()
    }
}
 */


class LocationService : Service() {

    companion object{
        const val CHANNEL_ID = "1234"
        const val NOTIFICATION_ID = 1234
    }

    private var fusedLocationProviderClient:FusedLocationProviderClient? = null
    private var locationCallback:LocationCallback?= null
    private var locationRequest:LocationRequest?= null
    private var notificationManager:NotificationManager?= null
    private var location:Location? = null

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,1000).setIntervalMillis(500).build()
        locationCallback = object :LocationCallback(){
            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult)
            }

        }

        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, "location", NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        // Check for permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request permission
            return
        }
    }

    @Suppress("MissingPermission")
    fun createLocationRequest(){
        try{
            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest!!,locationCallback!!, null)
            startForeground(NOTIFICATION_ID, getNotification()) // Start foreground service
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun removeLocationUpdates(){
        locationCallback?.let {
            fusedLocationProviderClient?.removeLocationUpdates(it)
        }
        stopForeground(true)
        stopSelf()
    }

    private fun onNewLocation(locationResult: LocationResult) {
        location = locationResult.lastLocation
        EventBus.getDefault().post(LocationEvent(
            latitude = location?.latitude,
            longitude = location?.longitude
        ))
    }

    fun getNotification():Notification{
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Updates")
            .setContentText("Latitude -> ${location?.latitude}\n Longitude -> ${location?.longitude}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)

        return notification.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        createLocationRequest()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        removeLocationUpdates()
    }
}




/*
class LocationService : Service() {

    private var locationManager: LocationManager? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // Get the location
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Check for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted, request permission
                return START_STICKY
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted, request permission
                return START_STICKY
            }
        }

        // Request location updates
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 (API level 29) and later
            fusedLocationProviderClient?.requestLocationUpdates(
                LocationRequest.create().setInterval(1000).setFastestInterval(500),
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            Log.d("Location", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                        }
                    }
                },
                null
            )
        } else {
            // Android 9 (API level 28) and earlier
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                500f,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        Log.d("Location", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }
            )
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop location updates
        fusedLocationProviderClient?.removeLocationUpdates(object : LocationCallback() {})
        locationManager?.removeUpdates(object : LocationListener {
            override fun onLocationChanged(location: Location) {
                TODO("Not yet implemented")
            }
        } as LocationListener)
    }
}
 */