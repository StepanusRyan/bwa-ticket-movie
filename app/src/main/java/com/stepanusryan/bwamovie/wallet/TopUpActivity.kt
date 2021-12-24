package com.stepanusryan.bwamovie.wallet

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.database.*
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.model.Wallet
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_top_up.*
import java.text.NumberFormat
import java.util.*

class TopUpActivity : AppCompatActivity() {
    var status25k:Boolean = false
    var status200k:Boolean = false
    private var nominal:String? = ""
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mFirebaseDatabaseReference: DatabaseReference
    lateinit var mFirebaseDatabaseReferenceTrans: DatabaseReference
    lateinit var preference: Preferences
    private var username:String? = ""
    private var balance:String? = ""
    private var saldo:Int = 0
    private var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)
        btnTopUpSekarang.visibility = View.INVISIBLE
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseDatabaseReference = mFirebaseDatabase.getReference("User")
        mFirebaseDatabaseReferenceTrans = mFirebaseDatabase.getReference("Transaksi")
        preference = Preferences(this)
        val localID = Locale("in","ID")
        val formats = NumberFormat.getCurrencyInstance(localID)
        balance = intent.getStringExtra("saldo")
        txtBalance.text = formats.format(balance?.toInt())


        val dataWallet = mFirebaseDatabaseReference.child(username!!).child("transaksi")
        Log.v("dt",dataWallet.toString())

        username = preference.getValues("username")
        btnDuaLima.setOnClickListener {
            if (status25k)
            {
                btnDuaLima.setBackgroundResource(R.drawable.shape_nominal)
                status25k = false
                btnTopUpSekarang.visibility = View.INVISIBLE
                nominal = "0"

                btnNominalLain.isClickable = false
            }
            else
            {
                btnDuaLima.setBackgroundResource(R.drawable.selected_nominal)
                status25k = true
                btnTopUpSekarang.visibility = View.VISIBLE
                nominal = "25000"

                btnNominalLain.isClickable = true
            }
        }
        btnDuaratus.setOnClickListener {
            if (status200k)
            {
                btnDuaratus.setBackgroundResource(R.drawable.shape_nominal)
                status200k = false
                btnTopUpSekarang.visibility = View.INVISIBLE
                nominal = "0"
                btnNominalLain.isClickable = false
            }
            else
            {
                btnDuaratus.setBackgroundResource(R.drawable.selected_nominal)
                status200k = true
                btnTopUpSekarang.visibility = View.VISIBLE
                nominal = "200000"

                btnNominalLain.isClickable = true
            }

        }

        saldo = preference.getValues("saldo")!!.toInt()



        btnNominalLain.setOnClickListener {
            //Toast.makeText(this@TopUpActivity, "ke halaman isi nominal",Toast.LENGTH_SHORT).show()
        }
        btnTopUpSekarang.setOnClickListener {
            topUp(nominal)
            updateSaldo(saldo,nominal)
            showNotif(nominal)
            startActivity(Intent(this@TopUpActivity, TopUpSuccessActivity::class.java))
            finish()
        }
    }

    private fun updateSaldo(saldos:Int, topup: String?) {
        val beli = topup!!.toInt()
        val total = saldos + beli
        mFirebaseDatabaseReference.child(username!!).child("saldo").setValue(total.toString())
        preference.setValues("saldo",total.toString())

    }

    private fun topUp(topup:String?) {
//        val fd = SimpleDateFormat("dd.MM.yyyy")
        val num = random(1,100000)
        val date = Calendar.getInstance().time.toString()
        val transaksi = Wallet()
        transaksi.action = "topup"
        transaksi.nominal = topup
        transaksi.date = date
        transaksi.id = "tp$num"

        inputTransaksi("tp$num",transaksi)

        mFirebaseDatabaseReference.child(username!!).child("saldo").setValue(topup)

        preference.setValues("saldo",topup.toString())
    }

    private fun inputTransaksi(s: String, transaksi: Wallet) {
        mFirebaseDatabaseReferenceTrans.child(s).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.getValue(Wallet::class.java)

                if (users == null)
                {
                    mFirebaseDatabaseReferenceTrans.child(username!!).child(s).setValue(transaksi)
                }
                else
                {
                    Toast.makeText(this@TopUpActivity, "User sudah digunakan",Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TopUpActivity, error.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showNotif(datas: String?)
    {
        val notificationChannelID = "channel_bwa_notif"
        val context = this.applicationContext
        var notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            val channelName = "BWA Movie Notif Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(notificationChannelID,channelName,importance)
            notificationManager.createNotificationChannel(mChannel)
        }
//        val mIntent = Intent(this,CheckoutSuccessActivity::class.java)
//        val bundle = Bundle()
//        bundle.putString("id","id_film")
//        mIntent.putExtras(bundle)
        val mIntent = Intent(this, TopUpSuccessActivity::class.java)
        val pendingIntent =
                PendingIntent.getActivity(this,0,mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this,notificationChannelID)
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo_mov)
                .setLargeIcon(
                        BitmapFactory.decodeResource(
                                this.resources,
                                R.drawable.logo_besar
                        )
                )
                .setTicker("notif bwa starting")
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
                .setLights(Color.RED,3000,3000)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("Pembelian Sukses")
                .setContentText("Top up $datas berhasil.")
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115,builder.build())
    }
    fun random(a:Int,b:Int):Int{
        return (a..b).random()
    }
}