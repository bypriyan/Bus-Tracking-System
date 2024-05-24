package com.bypriyan.bustrackingsystem.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bypriyan.bustrackingsystem.DashbordActivity
import com.bypriyan.bustrackingsystem.R
import com.bypriyan.bustrackingsystem.SaveUser
import com.bypriyan.bustrackingsystem.databinding.ActivitySelectBinding
import com.bypriyan.bustrackingsystem.driverActivity.DriverDashbordActivity
import com.bypriyan.bustrackingsystem.userActivity.UserDashbordActivity
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.PreferenceManager
import com.google.firebase.auth.FirebaseAuth

import javax.inject.Inject
class SelectActivity : AppCompatActivity() {

    lateinit var binding:ActivitySelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //splash screen
        installSplashScreen()

        val collegeName = intent.getStringExtra(Constants.KEY_COLLEGE_NAME)

        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        //back pressed
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        binding.registerUser.setOnClickListener{
            var intent =Intent(this, RegisterUserActivity::class.java)
            intent.putExtra(Constants.KEY_USER_TYPE,"user")
            startActivity(intent)
        }

        binding.registerAdmin.setOnClickListener{
            var intent =Intent(this, DashbordActivity::class.java)
            intent.putExtra(Constants.KEY_USER_TYPE,"user")
            startActivity(intent)
        }


        binding.registerDriver.setOnClickListener{
            var intent =Intent(this, RegisterDriverActivity::class.java)
            intent.putExtra(Constants.KEY_USER_TYPE,"driver")
            intent.putExtra(Constants.KEY_COLLEGE_NAME, collegeName)
            startActivity(intent)
        }

    }

}