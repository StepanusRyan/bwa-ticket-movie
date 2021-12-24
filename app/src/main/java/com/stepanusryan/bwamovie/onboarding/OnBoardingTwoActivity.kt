package com.stepanusryan.bwamovie.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepanusryan.bwamovie.R
import kotlinx.android.synthetic.main.activity_on_boarding_two.*

class OnBoardingTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_two)

        btnNext2.setOnClickListener {
            startActivity(Intent(this,OnBoardingThreeActivity::class.java))
        }
    }
}