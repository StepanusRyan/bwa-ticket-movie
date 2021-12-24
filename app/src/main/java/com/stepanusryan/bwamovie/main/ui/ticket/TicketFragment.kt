package com.stepanusryan.bwamovie.main.ui.ticket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.main.ui.home.ComingSoonAdapter
import com.stepanusryan.bwamovie.model.Film
import com.stepanusryan.bwamovie.model.Tiket
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.fragment_ticket.*

class TicketFragment : Fragment() {

    private lateinit var ticketViewModel: TicketViewModel
    private lateinit var preference: Preferences
    private lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<Tiket>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        ticketViewModel =
                ViewModelProvider(this).get(TicketViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_ticket, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
//        ticketViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preference = Preferences(requireContext())
        mDatabase = FirebaseDatabase.getInstance().getReference("Tiket")

        rv_ticket.layoutManager = LinearLayoutManager(context)
        getData()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (getDataSnapshot in snapshot.children)
                {
                    var film = getDataSnapshot.getValue(Tiket::class.java)
                    if (film != null) {
                        dataList.add(film)
                    }
                    rv_ticket.adapter = TiketAdapter(dataList){
                        var intent = Intent(context, TicketActivity::class.java).putExtra("data",it)
                        startActivity(intent)
                    }
                }
                txtTotal.text = "${dataList.size} Movies"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message,Toast.LENGTH_LONG).show()
            }

        })
    }
}