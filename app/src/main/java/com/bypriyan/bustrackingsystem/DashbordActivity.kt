package com.bypriyan.bustrackingsystem

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bypriyan.bustrackingsystem.adapter.AdapterBuses
import com.bypriyan.bustrackingsystem.databinding.ActivityDashbordBinding
import com.bypriyan.bustrackingsystem.databinding.ActivityUserDashbordBinding
import com.bypriyan.bustrackingsystem.model.ModelBusDetails
import com.bypriyan.bustrackingsystem.register.SelectCollegeActivity
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class DashbordActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashbordBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashbordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        // Call the function to fetch the list of buses
        authViewModel.getAllBusList()

        binding.imgLogOut.setOnClickListener{
            logout()
        }

        loadTime()

    }

    private fun loadTime() {
        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)

        if (timeOfDay >= 0 && timeOfDay < 12) {
            binding.time.setText("Hey! Good Morning" + " \uD83D\uDE0A")
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            binding.time.setText("Hey! Good Afternoon" + " \uD83C\uDF1E")
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            binding.time.setText("Hey! Good Evening" + " \uD83C\uDF1B")
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            binding.time.setText("Hey! Good Night" + " \uD83C\uDF1B")
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