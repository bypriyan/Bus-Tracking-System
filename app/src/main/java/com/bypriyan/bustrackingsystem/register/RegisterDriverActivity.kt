package com.bypriyan.bustrackingsystem.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bypriyan.bustrackingsystem.R
import com.bypriyan.bustrackingsystem.databinding.ActivityRegisterDriverBinding
import com.bypriyan.bustrackingsystem.databinding.ActivitySelectBinding
import com.bypriyan.bustrackingsystem.utility.Constants

class RegisterDriverActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterDriverBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_driver)

        binding = ActivityRegisterDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        val collegeName = intent.getStringExtra(Constants.KEY_COLLEGE_NAME)

        //back pressed
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        binding.continueBtn.setOnClickListener{

            if(isValid()){
                Log.d("TAG", "onCreate: ${binding.phoneNumber.editText?.text}")
                var intent = Intent(this, OTPActivity::class.java)
                intent.putExtra(Constants.KEY_NAME, binding.name.editText?.text.toString())
                intent.putExtra(Constants.KEY_PHONE_NUMBER, binding.phoneNumber.editText?.text.toString())
                intent.putExtra(Constants.KEY_VEHICLE_NUMBER, binding.vehicleNumber.editText?.text.toString())
                intent.putExtra(Constants.KEY_COLLEGE_NAME, collegeName)
                startActivity(intent)
            }
        }

    }

    private fun isValid(): Boolean {
        return when {
            binding.name.editText?.text?.isEmpty() == true -> {
                binding.name.editText?.requestFocus()
                false
            }
            (binding.phoneNumber.editText?.text?.isEmpty() == true || binding.phoneNumber.editText?.text?.length!=10) -> {
                binding.phoneNumber.editText?.requestFocus()
                false
            }
            binding.vehicleNumber.editText?.text?.isEmpty() == true -> {
                binding.vehicleNumber.editText?.requestFocus()
                false
            }
            else -> true
        }
    }


}