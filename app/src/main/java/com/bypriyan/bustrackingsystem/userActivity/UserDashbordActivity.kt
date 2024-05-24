package com.bypriyan.bustrackingsystem.userActivity

import android.content.Intent
import android.graphics.ColorSpace.Model
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bypriyan.bustrackingsystem.R
import com.bypriyan.bustrackingsystem.adapter.AdapterBuses
import com.bypriyan.bustrackingsystem.databinding.ActivityOtpactivityBinding
import com.bypriyan.bustrackingsystem.databinding.ActivityUserDashbordBinding
import com.bypriyan.bustrackingsystem.model.ModelBusDetails
import com.bypriyan.bustrackingsystem.register.SelectCollegeActivity
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserDashbordActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserDashbordBinding
    private lateinit var authViewModel: AuthViewModel

    var listOfBus = mutableListOf<ModelBusDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashbordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        // Call the function to fetch the list of buses
        authViewModel.getAllBusList()


        val ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_DRIVERS)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listOfBus = mutableListOf<ModelBusDetails>()
                snapshot.children.forEach { dataSnapshot ->
                    val busDetails = dataSnapshot.getValue(ModelBusDetails::class.java)
                    busDetails?.let {
                        listOfBus.add(it)
                    }
                }
                // Now that you have fetched the data, set the adapter for the RecyclerView
                binding.busRV.adapter = AdapterBuses(this@UserDashbordActivity, listOfBus)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled if needed
            }
        })

        binding.backBtn.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        //back pressed
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        binding.logoutBtn.setOnClickListener{
            logout()
        }

    }

    fun logout(){
        var firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        var intent = Intent(this, SelectCollegeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}