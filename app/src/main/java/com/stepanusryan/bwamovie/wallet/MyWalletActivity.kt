package com.stepanusryan.bwamovie.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.model.Wallet
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_my_wallet.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MyWalletActivity : AppCompatActivity() {
    lateinit var preference: Preferences
    private var datalist = ArrayList<Wallet>()
    private lateinit var mDatabase:DatabaseReference
    private var username:String? = ""
    private var saldo:String? = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)
        preference = Preferences(this)
        username = preference.getValues("username")
        mDatabase = FirebaseDatabase.getInstance().getReference("Transaksi").child(username!!)
        val localID = Locale("in","ID")
        val formats = NumberFormat.getCurrencyInstance(localID)
//        datalist.add(
//            Wallet(
//                "Avengers",
//                "Mei 2021",
//                70000.0,
//                "0"
//            )
//        )

        rv_transaksi.layoutManager = LinearLayoutManager(this@MyWalletActivity)
        dataTransaksi()

        txtBalance.text = formats.format( preference.getValues("saldo")?.toInt())
        saldo = preference.getValues("saldo")
        btnTopUp.setOnClickListener {
            startActivity(Intent(this@MyWalletActivity, TopUpActivity::class.java).putExtra("saldo",saldo.toString()))
        }
    }

    private fun dataTransaksi() {
        mDatabase.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                datalist.clear()
                for (getDataSnapshot in snapshot.children)
                {
                    var transaksi = getDataSnapshot.getValue(Wallet::class.java)
                    if (transaksi != null)
                    {
                        datalist.add(transaksi)
                    }
                }
                // code dibawah ini berfungsi untuk sortir transaksi by tanggal
                // tanggal paling baru berada di paling atas
                var sortList = datalist.sortedWith(compareByDescending { it.date })
                Log.d("DATA",""+sortList)
                rv_transaksi.adapter = TransaksiAdapter(sortList){

                }
            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(this@MyWalletActivity, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}