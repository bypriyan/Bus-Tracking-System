package com.bypriyan.bustrackingsystem.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bypriyan.bustrackingsystem.R
import com.bypriyan.bustrackingsystem.databinding.ActivityRegisterDriverBinding
import com.bypriyan.bustrackingsystem.databinding.ActivityRegisterUserBinding
import com.bypriyan.bustrackingsystem.utility.Constants

class RegisterUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.continueBtn.setOnClickListener{

            if(isValid()){
                Log.d("TAG", "onCreate: ${binding.phoneNumber.editText?.text}")
                var intent = Intent(this, UserOTPActivity::class.java)
                intent.putExtra(Constants.KEY_PHONE_NUMBER, binding.phoneNumber.editText?.text.toString())
                startActivity(intent)
            }
        }


    }


    private fun isValid(): Boolean {
        return when {
            (binding.phoneNumber.editText?.text?.isEmpty() == true || binding.phoneNumber.editText?.text?.length!=10) -> {
                binding.phoneNumber.editText?.requestFocus()
                false
            }
            else -> true
        }
    }


}