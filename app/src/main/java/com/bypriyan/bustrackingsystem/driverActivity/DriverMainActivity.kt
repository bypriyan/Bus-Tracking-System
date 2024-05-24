package com.bypriyan.bustrackingsystem.driverActivity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bypriyan.bustrackingsystem.R
import com.bypriyan.bustrackingsystem.databinding.ActivityDriverDashbordBinding
import com.bypriyan.bustrackingsystem.databinding.ActivityDriverMainBinding
import com.bypriyan.bustrackingsystem.databinding.ActivityOtpactivityBinding
import com.bypriyan.bustrackingsystem.viewModel.AuthViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class DriverMainActivity : AppCompatActivity() {


    lateinit var binding: ActivityDriverMainBinding
    private var service: Intent? = null
    private lateinit var authViewModel: AuthViewModel
    


    private val backgroundLocation = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            startService(service)
        } else {
            Log.d("TAG", "Background location permission denied")
        }
    }

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
        enableEdgeToEdge()
        setContentView(R.layout.activity_driver_main)

        //init
        binding = ActivityDriverMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

         authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        binding.submitBtn.setOnClickListener {
//            checkPermission()
//        }

//        startActivity(Intent(this, DriverDashbordActivity::class.java))

        service = Intent(this, LocationService::class.java)
    }

    fun checkPermission() {
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
                startService(service)
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
                startService(service)
            }
        } else {
            // Android 5 (API level 22) and earlier
            startService(service)
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
    }

}