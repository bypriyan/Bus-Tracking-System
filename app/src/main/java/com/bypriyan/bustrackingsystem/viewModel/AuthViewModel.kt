package com.bypriyan.bustrackingsystem.viewModel

import android.app.Activity
import android.graphics.RectF
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bypriyan.bustrackingsystem.adapter.AdapterBuses
import com.bypriyan.bustrackingsystem.model.ModelBusDetails
import com.bypriyan.bustrackingsystem.model.ModelLocationTrack
import com.bypriyan.bustrackingsystem.utility.Constants
import com.google.common.io.Closeables.close
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    //verificationId
    private val _verificationId = MutableLiveData<String>()
    val verificationId: LiveData<String> = _verificationId
    //codeCheck
    private val _authStatus = MutableLiveData<Boolean>()
    val authStatus: LiveData<Boolean> = _authStatus
    //datasave
    private val _dataSaveStatus = MutableLiveData<Boolean>()
    val authDataSave: LiveData<Boolean> = _dataSaveStatus
    //userList
    private val _allBuses = MutableLiveData<List<ModelBusDetails>>()
    val allBuses: LiveData<List<ModelBusDetails>> = _allBuses
    //busLocation
    private val _busLocation = MutableLiveData<ModelLocationTrack>()
    val busLocation: LiveData<ModelLocationTrack> = _busLocation

    fun authenticatePhoneNumber(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            Log.d("TAG", "authenticatePhoneNumber: ${Thread.currentThread().name}")
            withContext(Dispatchers.IO) {
                Log.d("TAG", "authenticatePhoneNumber: ${Thread.currentThread().name}")
                var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
                val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        Log.d(
                            "TAG",
                            "onVerificationCompleted: this is on verification complete ${p0.smsCode.toString()} " + Thread.currentThread().name
                        )
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {

                        Log.d(
                            "TAG",
                            "onVerificationFailed: this is on verification failed ${p0.message}" + Thread.currentThread().name
                        )
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(p0, p1)
                        Log.d(
                            "TAG",
                            "onVerificationCompleted: this is on verification complete ${p0}     " + Thread.currentThread().name
                        )
                        // Update the verification ID
                        _verificationId.value = p0
                    }
                }

                val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber(phoneNumber) // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(activity)
                    .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }


        }
    }

    fun authenticateOTP(verificationId: String, code:String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val credential = PhoneAuthProvider.getCredential(verificationId, code)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _authStatus.value = true
                        } else {
                            _authStatus.value= false
                        }
                    }
            }

        }
    }

    fun saveDriverDataToFirebase(name: String, phoneNumber:String, busNumber:String, collegeName:String, busLat:String="0.0", busLong:String="0.0") {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                var uid:String = FirebaseAuth.getInstance().currentUser?.uid.toString()

                var map = mutableMapOf<String, String>()
                map.put(Constants.KEY_NAME, name)
                map.put(Constants.KEY_PHONE_NUMBER, phoneNumber)
                map.put(Constants.KEY_VEHICLE_NUMBER, busNumber)
                map.put(Constants.KEY_BUS_LATITUDE, busLat)
                map.put(Constants.KEY_BUS_LONGITUDE, busLong)
                map.put(Constants.KEY_COLLEGE_NAME, collegeName)
                map.put(Constants.KEY_UID, uid)

               var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.KEY_DRIVERS)
                   reference.child(uid).setValue(map).addOnSuccessListener {
                       _dataSaveStatus.value = true
                   }.addOnFailureListener{
                       _dataSaveStatus.value = false
                       Log.d("TAG", "saveDriverDataToFirebase: $it")
                   }

            }

        }
    }

    fun updateDriverLocationToFirebase(busLat:String="0.0", busLong:String="0.0") {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                var uid:String = FirebaseAuth.getInstance().currentUser?.uid.toString()

                var map = mutableMapOf<String, String>()
                map.put(Constants.KEY_BUS_LATITUDE, busLat)
                map.put(Constants.KEY_BUS_LONGITUDE, busLong)

                var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.KEY_DRIVERS)
                reference.child(uid).updateChildren(map as Map<String, Any>).addOnSuccessListener {
                    Log.d("TAG", "updateDriverLocationToFirebase: data updated")
                }.addOnFailureListener{
                    Log.d("TAG", "saveDriverDataToFirebase: $it")
                }

            }

        }
    }

    fun getAllBusList(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                var listOfBus = mutableListOf<ModelBusDetails>()

                val ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_DRIVERS)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfBus.clear()
                snapshot.children.forEach {
                    val busDetails = it.getValue(ModelBusDetails::class.java)
                    listOfBus.add(busDetails!!)
                    Log.d("TAG", "onDataChange: $busDetails")
                }

                _allBuses.value = listOfBus
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        // Add the ValueEventListener to the database reference
        ref.addListenerForSingleValueEvent(listener)

            }
        }

    }

    fun getBusLatLongLocation( uidOfDriver:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_DRIVERS)

                ref.child(uidOfDriver).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val busDetails = snapshot.getValue(ModelBusDetails::class.java)
                        var latitude =busDetails?.lat?.toDouble()
                        var longitude = busDetails?.long?.toDouble()
                        busDetails?.let {
                           _busLocation.value =  ModelLocationTrack(latitude!!, longitude!!)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle onCancelled if needed
                    }
                })

            }
        }

    }

//    fun getAllBusList() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                getAllBuses().collect { buses ->
//                    _allBuses.value =buses
//                }
//            }
//        }
//    }
//
//    private suspend fun getAllBuses(): Flow<ModelBusDetails> = callbackFlow {
//        val ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_DRIVERS)
//        val listener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach {
//                    val busDetails = it.getValue(ModelBusDetails::class.java)
//                    trySend(busDetails!!)
//                    Log.d("TAG", "onDataChange: $busDetails")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                close(error.toException()) // Close the flow in case of error
//            }
//        }
//
//        // Add the ValueEventListener to the database reference
//        ref.addListenerForSingleValueEvent(listener)
//
//        // Use awaitClose to properly handle cancellation
//        awaitClose {
//            ref.removeEventListener(listener)
//        }
//    }



}

