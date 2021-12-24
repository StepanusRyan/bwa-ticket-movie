package com.stepanusryan.bwamovie.pilihbangku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.checkout.CheckoutActivity
import com.stepanusryan.bwamovie.model.Checkout
import com.stepanusryan.bwamovie.model.Film
import kotlinx.android.synthetic.main.activity_choose_seat.*

class ChooseSeatActivity : AppCompatActivity() {
    var statusA1:Boolean = false
    var statusA2:Boolean = false
    var total:Int = 0
    private var dataList = ArrayList<Checkout>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_seat)
        var data = intent.getParcelableExtra<Film>("data")
        txtMovie.text = data?.judul
        var color = ContextCompat.getColor(this,R.color.color_red)

        btnA1.setOnClickListener {
            if (statusA1)
            {
                btnA1.setBackgroundResource(R.drawable.custom_seat)
                statusA1 = false
                total -= 1

                beliTiket(total)
                dataList.remove(Checkout("A1","70000"))
            }
            else
            {
                btnA1.setBackgroundColor(color)
                statusA1 = true
                total += 1

                beliTiket(total)
                val data = Checkout("A1","70000")
                dataList.add(data)
            }
        }
        btnA2.setOnClickListener {
            if (statusA2)
            {
                btnA2.setBackgroundResource(R.drawable.custom_seat)
                statusA2 = false
                total -= 1

                beliTiket(total)
                dataList.remove(Checkout("A2","70000"))
            }
            else
            {
                btnA2.setBackgroundColor(color)
                statusA2 = true
                total += 1

                beliTiket(total)
                val data = Checkout("A2","70000")
                dataList.add(data)
            }
        }
        btnBuyTicket.setOnClickListener {
            var intent = Intent(this@ChooseSeatActivity, CheckoutActivity::class.java).putExtra("data",dataList).putExtra("datas",data)
            startActivity(intent)
            finish()
        }
    }

    private fun beliTiket(total: Int) {
        if (total == 0)
        {
            btnBuyTicket.setText("Beli Tiket")
            btnBuyTicket.visibility = View.INVISIBLE
        }
        else
        {
            btnBuyTicket.setText("Beli Tiket ( " + total + " )")
            btnBuyTicket.visibility = View.VISIBLE
        }

    }
}