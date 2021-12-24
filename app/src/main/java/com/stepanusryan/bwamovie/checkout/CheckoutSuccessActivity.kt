package com.stepanusryan.bwamovie.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.main.MainActivity
import kotlinx.android.synthetic.main.activity_checkout_success.*

class CheckoutSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)

        btnSkip1.setOnClickListener {
            startActivity(Intent(this@CheckoutSuccessActivity, MainActivity::class.java))
            finish()
        }
        btnNext1.setOnClickListener {

        }
    }
}