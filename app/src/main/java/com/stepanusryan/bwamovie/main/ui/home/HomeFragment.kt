package com.stepanusryan.bwamovie.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.detailmovie.DetailMovieActivity
import com.stepanusryan.bwamovie.model.Coming
import com.stepanusryan.bwamovie.model.Film
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var preference: Preferences
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mData: DatabaseReference
    private val dataList = ArrayList<Film>()
    private val dataComing = ArrayList<Coming>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preference = Preferences(requireActivity().applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")
        mData = FirebaseDatabase.getInstance().getReference("Coming")
        txtName.text = preference.getValues("name")
        if (!preference.getValues("saldo").equals(""))
        {
            currency(preference.getValues("saldo")!!.toDouble(), txtBalance)
        }
        Glide.with(this)
                .load(preference.getValues("url"))
                .into(imageProfile)
        recyclerNowPlaying.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        recyclerComingSoon.layoutManager = LinearLayoutManager(context)
        recyclerComingSoon.setHasFixedSize(true)
        getDataFilm()
        getDataComing()
    }

    private fun getDataFilm() {
        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (getDataSnapshot in dataSnapshot.children)
                {
                    var film = getDataSnapshot.getValue(Film::class.java)
                    if (film != null) {
                        dataList.add(film)
                    }
                }
                recyclerNowPlaying.adapter = NowPlayingAdapter(dataList){
                    var intent = Intent(activity,DetailMovieActivity::class.java).putExtra("data",it)
                    startActivity(intent)

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity,""+databaseError.message,Toast.LENGTH_LONG).show()
            }

        })
    }
    private fun getDataComing() {
        mData.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataComing.clear()
                for (getDataSnapshot in snapshot.children){
                    var coming = getDataSnapshot.getValue(Coming::class.java)
                    if (coming != null){
                        dataComing.add(coming)
                    }
                }
                recyclerComingSoon.adapter = ComingSoonAdapter(dataComing){
//                    var intent = Intent(activity,DetailMovieActivity::class.java).putExtra("data",it)
//                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,""+error.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun currency(balance:Double, textView: TextView ){
        val localID = Locale("in","ID")
        val formats = NumberFormat.getCurrencyInstance(localID)
        textView.text = formats.format(balance)
    }
}