package com.bypriyan.bustrackingsystem.driverActivity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bypriyan.bustrackingsystem.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.bypriyan.bustrackingsystem.databinding.ActivityDriverDashbordBinding
import com.bypriyan.bustrackingsystem.register.SelectActivity
import com.bypriyan.bustrackingsystem.register.SelectCollegeActivity
import com.bypriyan.bustrackingsystem.viewModel.AuthViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.auth.FirebaseAuth
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class DriverDashbordActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDriverDashbordBinding

    private var service: Intent? = null
    private lateinit var authViewModel: AuthViewModel
    private var isClicked:Boolean = false

    private var marker: MarkerOptions? = null

    private val locationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        when {
            it.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Handle fine location permission
            }
            it.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Handle coarse location permission
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDriverDashbordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mp) as SupportMapFragment
        mapFragment.getMapAsync(this)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        service = Intent(this, LocationService::class.java)

        binding.startBtn.setOnClickListener {
            if(!isClicked){
                checkPermission()
                if(isLocationEnabled()){
                    isClicked = true
                    binding.startBtn.setText("Stop")
                }

            }else{
                stopService(service)
                binding.startBtn.setText("Start")
                isClicked = false
            }

        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                finish()
                finishAffinity()
            }
        })

        binding.logoutBtn.setOnClickListener{
            logout()
        }

    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 (API level 29) and later
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermission.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            } else {
                if (isLocationEnabled()) {
                    startService(service)
                } else {
                    showLocationDialog()
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6 (API level 23) and later
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermission.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            } else {
                if (isLocationEnabled()) {
                    startService(service)
                } else {
                    showLocationDialog()
                }
            }
        } else {
            // Android 5 (API level 22) and earlier
            if (isLocationEnabled()) {
                startService(service)
            } else {
                showLocationDialog()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(service)
    }

    @Subscribe
    fun reciveLocationEvent(locationEvent: LocationEvent){
//        binding.phoneLbl.text = "lat - > "+locationEvent.latitude+" long - >"+locationEvent.longitude
        Log.d("TAG", "reciveLocationEvent: lat - > ${locationEvent.latitude} long - >${locationEvent.longitude}")
        authViewModel.updateDriverLocationToFirebase("${locationEvent.latitude}", "${locationEvent.longitude}")
//        loadMap(locationEvent.latitude!!, locationEvent.longitude!!)
        updateMarkerPosition(LatLng(locationEvent.latitude!!, locationEvent.longitude!!))

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney
        marker = MarkerOptions().position(LatLng(0.0, 0.0)).title("Your Location")
        mMap.addMarker(marker!!)
    }

    private fun updateMarkerPosition(position: LatLng) {
        // Remove previous marker
        mMap.clear()

        // Add new marker
        marker?.position(position)
        mMap.addMarker(marker!!)

        // Move camera to new marker position
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position))
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showLocationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Location is not enabled")
            .setMessage("Please enable location to use this feature")
            .setPositiveButton("Enable") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Handle cancel
            }
            .show()
    }

    fun logout(){
        var firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        var intent = Intent(this, SelectCollegeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        finish()
    }


}