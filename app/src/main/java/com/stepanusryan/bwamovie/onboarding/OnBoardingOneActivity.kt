package com.stepanusryan.bwamovie.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.signin.SignInActivity
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_on_boarding_one.*

class OnBoardingOneActivity : AppCompatActivity() {
    lateinit var preferences:Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_one)

        preferences = Preferences(this)
        if (preferences.getValues("onboard").equals("1"))
        {
            finishAffinity()
            startActivity(Intent(this@OnBoardingOneActivity, SignInActivity::class.java))
        }

        btnNext1.setOnClickListener {
            startActivity(Intent(this@OnBoardingOneActivity,OnBoardingTwoActivity::class.java))
        }
        btnSkip1.setOnClickListener {
            preferences.setValues("onboard","1")
            finishAffinity()
            startActivity(Intent(this@OnBoardingOneActivity,SignInActivity::class.java))

        }
    }
}