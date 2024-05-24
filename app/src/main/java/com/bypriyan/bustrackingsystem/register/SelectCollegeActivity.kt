package com.bypriyan.bustrackingsystem.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bypriyan.bustrackingsystem.R
import com.bypriyan.bustrackingsystem.adapter.AdapterColleges
import com.bypriyan.bustrackingsystem.databinding.ActivitySelectBinding
import com.bypriyan.bustrackingsystem.databinding.ActivitySelectCollegeBinding
import com.bypriyan.bustrackingsystem.driverActivity.DriverDashbordActivity
import com.bypriyan.bustrackingsystem.model.ModelColleges
import com.bypriyan.bustrackingsystem.userActivity.UserDashbordActivity
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.PreferenceManager
import com.google.firebase.auth.FirebaseAuth

class SelectCollegeActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectCollegeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivitySelectCollegeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCollegeList()

        binding.backBtn.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        //back pressed
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.collegeRv.adapter = AdapterColleges(this, loadCollegeList())

    }

    private fun loadCollegeList():List<ModelColleges> {

        return listOf(
            ModelColleges("https://upload.wikimedia.org/wikipedia/en/7/72/Central_University_of_Odisha_logo.png","The Central University Of Odisha"),
            ModelColleges("https://upload.wikimedia.org/wikipedia/en/7/74/Berhampur-University_Logo.png","Berhampur University"),
            ModelColleges("https://upload.wikimedia.org/wikipedia/en/e/ec/Official_logo_of_Biju_Patanaik_University_of_Technology.png","Biju Patnaik University of Technology"),
            ModelColleges("https://upload.wikimedia.org/wikipedia/en/c/c1/Sambalpur_University_logo.png","Sambalpur University"),
            ModelColleges("https://upload.wikimedia.org/wikipedia/en/thumb/b/b2/AIIMS_Bhubaneswar_logo.png/220px-AIIMS_Bhubaneswar_logo.png","All India Institute of Medical Sciences"),

            ModelColleges("https://upload.wikimedia.org/wikipedia/en/b/b6/Madhusudan_Law_University_Logo.png","Madhusudan Law University"),
            ModelColleges("https://kiit.ac.in/wp-content/uploads/2019/02/cropped-KIIT-Logo-500x500-2.png","Kalinga Institute of Industrial Technology"),
            ModelColleges("https://upload.wikimedia.org/wikipedia/en/thumb/7/73/Logo_vssut.svg/1200px-Logo_vssut.svg.png","Veer Surendra Sai University of Technology"),
            ModelColleges("https://upload.wikimedia.org/wikipedia/en/thumb/c/cb/Rama_Devi_Women%27s_University_Logo.png/220px-Rama_Devi_Women%27s_University_Logo.png","Ramadevi Women's University"),
            ModelColleges("https://upload.wikimedia.org/wikipedia/en/thumb/f/f1/National_Law_University_Odisha_Logo.png/220px-National_Law_University_Odisha_Logo.png","National Law University Odisha"),


            )
    }

    override fun onStart() {
        super.onStart()
        var auth = FirebaseAuth.getInstance().currentUser
        auth?.let {
            var userType = PreferenceManager(this).getString(Constants.KEY_USER_TYPE)
            if (userType.equals(Constants.KEY_USER_DRIVER)){
                startActivity(Intent(this, DriverDashbordActivity::class.java))
            }else if(userType.equals(Constants.KEY_USER_USER)){
                startActivity(Intent(this, UserDashbordActivity::class.java))
            }
        }
    }

}