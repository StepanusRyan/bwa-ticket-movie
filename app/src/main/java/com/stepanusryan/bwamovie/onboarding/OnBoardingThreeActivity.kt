package com.stepanusryan.bwamovie.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_on_boarding_three.*

class OnBoardingThreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_three)
        btnNext3.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
            finishAffinity()
        }
    }
}