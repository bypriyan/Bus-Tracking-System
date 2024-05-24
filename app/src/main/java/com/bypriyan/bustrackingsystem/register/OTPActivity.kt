package com.bypriyan.bustrackingsystem.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bypriyan.bustrackingsystem.databinding.ActivityOtpactivityBinding
import com.bypriyan.bustrackingsystem.driverActivity.DriverDashbordActivity
import com.bypriyan.bustrackingsystem.driverActivity.DriverMainActivity
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.PreferenceManager
import com.bypriyan.bustrackingsystem.viewModel.AuthViewModel
import javax.inject.Inject

class OTPActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtpactivityBinding

    private lateinit var authViewModel: AuthViewModel
    private var verificationId:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init
        val phoneNumber = intent.getStringExtra(Constants.KEY_PHONE_NUMBER)
        val name = intent.getStringExtra(Constants.KEY_NAME)
        val vehicleNumber = intent.getStringExtra(Constants.KEY_VEHICLE_NUMBER)
        val userType = intent.getStringExtra(Constants.KEY_USER_TYPE)
        val collegeName = intent.getStringExtra(Constants.KEY_COLLEGE_NAME)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        phoneNumber?.let {
            binding.phoneLbl.setText("Verify +91 $it")
            authViewModel.authenticatePhoneNumber("+91$phoneNumber", this)
        }

        binding.submitBtn.setOnClickListener {
            verificationId?.let {
                var str:String =  binding.firstPinView.text.toString()
                if(str.length==6){
                    isLoading(true)
                    authViewModel.authenticateOTP(verificationId!!, str)
                }else{
                    isLoading(false)
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //back pressed
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        authViewModel.verificationId.observe(this, Observer { verificationId ->
            this.verificationId = verificationId
        })

        authViewModel.authStatus.observe(this, Observer{
            if(it){
                authViewModel.saveDriverDataToFirebase(name!!, phoneNumber!!, vehicleNumber!!, collegeName = collegeName!!)
            }else{
                isLoading(false)
                Toast.makeText(this, "failed", Toast.LENGTH_LONG).show()
            }
        })

        authViewModel.authDataSave.observe(this, Observer{
            if(it){
                isLoading(false)
                PreferenceManager(this).putString(Constants.KEY_USER_TYPE, Constants.KEY_USER_DRIVER )
                val intent = Intent(this, DriverDashbordActivity::class.java)
                // Add flags to clear previous activities from the stack
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        })


    }

    fun isLoading(loading:Boolean){
        if(loading){
            binding.submitBtn.visibility =View.GONE
            binding.progressbar.visibility = View.VISIBLE
        }else{
            binding.submitBtn.visibility =View.VISIBLE
            binding.progressbar.visibility = View.GONE
        }
    }



}