package com.stepanusryan.bwamovie.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.main.MainActivity
import kotlinx.android.synthetic.main.activity_top_up_success.*

class TopUpSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up_success)

        btnLihatWallet.setOnClickListener {
            startActivity(Intent(this@TopUpSuccessActivity, MyWalletActivity::class.java))
            finish()
        }
        btnHome.setOnClickListener {
            startActivity(Intent(this@TopUpSuccessActivity, MainActivity::class.java))
            finish()
        }
    }
}