package com.stepanusryan.bwamovie.wallet

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.model.Wallet
import java.text.NumberFormat
import java.util.*

class TransaksiAdapter(private var data: List<Wallet>,
                   private var listener: (Wallet) -> Unit)
    : RecyclerView.Adapter<TransaksiAdapter.ViewHolder>(){

    lateinit var contextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView = layoutInflater.inflate(R.layout.row_transaksi,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: TransaksiAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position],listener,contextAdapter)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txtTitle)
        private val date: TextView = view.findViewById(R.id.txtDate)
        private val transaksi: TextView = view.findViewById(R.id.txtTransaksi)

        fun bindItem(data: Wallet, listener: (Wallet) -> Unit, context: Context)
        {
            title.text = data.action
            date.text = data.date

            val localID = Locale("in","ID")
            val formats = NumberFormat.getCurrencyInstance(localID)
            if (data.action.equals("beli") ){
                transaksi.text = "-"+formats.format(data.nominal?.toInt())
                transaksi.setTextColor(Color.RED)
            }
            else
            {
                transaksi.text = "+"+formats.format(data.nominal?.toInt())
                transaksi.setTextColor(Color.GREEN)
            }
            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}