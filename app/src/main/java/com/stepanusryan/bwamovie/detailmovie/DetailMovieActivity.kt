package com.stepanusryan.bwamovie.detailmovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.model.Film
import com.stepanusryan.bwamovie.model.Plays
import com.stepanusryan.bwamovie.pilihbangku.ChooseSeatActivity
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<Plays>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val data = intent.getParcelableExtra<Film>("data")
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")
                .child(data?.judul.toString())
                .child("play")

        txtJudul.text = data?.judul
        txtGenre.text = data?.genre
        txtDesc.text = data?.desc
        rating.text = data?.rating
        Glide.with(this)
                .load(data?.poster)
                .into(imagePoster)

        rv_play.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        getData()
        btnPilihBangku.setOnClickListener {
            var intent = Intent(this@DetailMovieActivity, ChooseSeatActivity::class.java).putExtra("data",data)
            startActivity(intent)
            finish()
        }
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (getDataSnapshot in snapshot.children)
                {
                    var film = getDataSnapshot.getValue(Plays::class.java)
                    if (film != null) {
                        dataList.add(film)
                    }

                }
                rv_play.adapter = PlaysAdapter(dataList){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailMovieActivity,""+error.message,Toast.LENGTH_LONG).show()
            }

        })
    }
}