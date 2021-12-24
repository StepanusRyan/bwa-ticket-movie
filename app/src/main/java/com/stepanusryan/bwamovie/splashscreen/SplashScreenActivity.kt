package com.stepanusryan.bwamovie.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.onboarding.OnBoardingOneActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this,OnBoardingOneActivity::class.java))
            finish()
        },5000L)

    }
}