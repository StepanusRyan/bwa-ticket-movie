package com.stepanusryan.bwamovie.checkout

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.main.ui.ticket.TicketActivity
import com.stepanusryan.bwamovie.model.Checkout
import com.stepanusryan.bwamovie.model.Film
import com.stepanusryan.bwamovie.model.Wallet
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {
    var datalist = ArrayList<Checkout>()
    private var total:Int = 0
    private lateinit var preferences: Preferences
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mFirebaseDatabaseReference: DatabaseReference
    lateinit var mFirebaseDatabaseReferenceSaldo: DatabaseReference
    lateinit var balancehint:String
    lateinit var balance:String
    private var saldo:Int = 0
    private var username:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseDatabaseReference = mFirebaseDatabase.getReference("Transaksi")
        mFirebaseDatabaseReferenceSaldo = mFirebaseDatabase.getReference("User")

        preferences = Preferences(this)
        username = preferences.getValues("username")
        balancehint = resources.getString(R.string.balance_detail)
        datalist = intent.getSerializableExtra("data") as ArrayList<Checkout>
        val data = intent.getParcelableExtra<Film>("datas")
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

        for (a in datalist.indices)
        {
            total+= datalist[a].harga!!.toInt()
        }
        datalist.add(Checkout("Total yang harus dibayar", total.toString()))

        rv_checkout.layoutManager = LinearLayoutManager(this)
        rv_checkout.adapter = CheckoutAdapter(datalist){

        }
        balance = preferences.getValues("saldo").toString()
        txtSaldo.text = ""+formatRupiah.format(balance.toInt())
        saldo = Integer.parseInt(balance)
        Toast.makeText(this@CheckoutActivity, ""+saldo,Toast.LENGTH_LONG).show()

        if (saldo <= total)
        {
            btnBuy.visibility = View.INVISIBLE
            txtBalanceHint.visibility = View.VISIBLE
            txtBalanceHint.text = balancehint
        }
        else
        {
            btnBuy.visibility = View.VISIBLE
            txtBalanceHint.visibility = View.INVISIBLE
        }

        btnBuy.setOnClickListener {
            beli(total,saldo)
            startActivity(Intent(this@CheckoutActivity, CheckoutSuccessActivity::class.java))
            showNotif(data!!)
            finish()
        }
        btnCancel.setOnClickListener {
            finish()
        }
    }
    private fun beli(beli:Int?,saldo:Int?) {
//        val fd = SimpleDateFormat("dd.MM.yyyy")
        val num = random(1,100000)
        val date = Calendar.getInstance().time.toString()

        val transaksi = Wallet()
        transaksi.action = "beli"
        transaksi.nominal = beli.toString()
        transaksi.date = date
        transaksi.id = "tp$num"

        val balance = saldo!! - beli!!

        inputTransaksi("tp$num",transaksi)
        updateSaldo(balance)
    }
    private fun updateSaldo(balance:Int){
        mFirebaseDatabaseReferenceSaldo.child(username!!).child("saldo").setValue("$balance")
        preferences.setValues("saldo",balance.toString())
    }
    private fun inputTransaksi(s: String, transaksi: Wallet) {
        mFirebaseDatabaseReference.child(s).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.getValue(Wallet::class.java)

                if (users == null)
                {
                    mFirebaseDatabaseReference.child(username!!).child(s).setValue(transaksi)
                }
                else
                {
                    Toast.makeText(this@CheckoutActivity, "User sudah digunakan",Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CheckoutActivity, error.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showNotif(datas:Film)
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
        val mIntent = Intent(this,TicketActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("data",datas)
        mIntent.putExtras(bundle)

        val pendingIntent =
                PendingIntent.getActivity(this,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT)

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
                .setContentText("Tiket ${datas.judul} berhasil kamu dapatkan.\nEnjoy the movie!")
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115,builder.build())
    }
    fun random(a : Int, b : Int) : Int {
        return (a..b).random()
    }
}