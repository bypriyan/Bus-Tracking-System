package com.bypriyan.bustrackingsystem.userActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bypriyan.bustrackingsystem.R
import com.bypriyan.bustrackingsystem.adapter.AdapterBuses

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.bypriyan.bustrackingsystem.databinding.ActivityUserMapTrackBusBinding
import com.bypriyan.bustrackingsystem.model.ModelBusDetails
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.viewModel.AuthViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserMapTrackBusActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityUserMapTrackBusBinding

    private lateinit var authViewModel: AuthViewModel
    private var marker: MarkerOptions? = null
    var vehNum:String? = null
    var busDriverUid:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMapTrackBusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        busDriverUid = intent.getStringExtra(Constants.KEY_UID)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
       vehNum = intent.getStringExtra(Constants.KEY_VEHICLE_NUMBER)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        authViewModel.getBusLatLongLocation(busDriverUid!!)

//        val ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_DRIVERS)
//        ref.child(busDriverUid!!).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                    val busDetails = snapshot.getValue(ModelBusDetails::class.java)
//                    var latitude =busDetails?.lat?.toDouble()
//                    var longitude = busDetails?.long?.toDouble()
//                    busDetails?.let {
//                        loadMap(latitude!!, longitude!!)
//                    }
//
//            }

//            override fun onCancelled(error: DatabaseError) {
//                // Handle onCancelled if needed
//            }
//        })

        authViewModel.busLocation.observe(this, Observer{modelBusLocationTrack->
//            loadMap(modelBusLocationTrack.latitude, modelBusLocationTrack.longitude)
            // Update marker position
            updateMarkerPosition(LatLng(modelBusLocationTrack.latitude, modelBusLocationTrack.longitude))
        })

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
//                finishAffinity()
            }
        })


    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        loadMap()
//    }
//    private fun loadMap(lat:Double=-34.0, longitude: Double=151.0) {
//        mMap.clear()
//        val sydney = LatLng(lat,longitude)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Your Location"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney
        marker = MarkerOptions().position(LatLng(0.0, 0.0)).title(vehNum)
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

}