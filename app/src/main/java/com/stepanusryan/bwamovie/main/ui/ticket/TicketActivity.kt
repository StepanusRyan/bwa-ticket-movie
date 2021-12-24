package com.stepanusryan.bwamovie.main.ui.ticket

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.model.Checkout
import com.stepanusryan.bwamovie.model.Film
import com.stepanusryan.bwamovie.model.Tiket
import kotlinx.android.synthetic.main.activity_ticket.*

class TicketActivity : AppCompatActivity() {
    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        var data = intent.getParcelableExtra<Tiket>("data")
        txtTitle.text = data?.judul
        txtGenre.text = data?.genre
        txtRating.text = "5"
        Glide.with(this)
            .load(data?.poster)
            .into(imagePoster)

        rv_list_ticket.layoutManager = LinearLayoutManager(this)
        dataList.add(Checkout("A1",""))
        dataList.add(Checkout("A2",""))

        rv_list_ticket.adapter = TicketAdapter(dataList){

        }
        iv_barcode.setOnClickListener {
            showDialogs()
        }
    }

    private fun showDialogs() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.barcode_dialog)
        val btnClose = dialog.findViewById(R.id.btnClose) as Button

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}