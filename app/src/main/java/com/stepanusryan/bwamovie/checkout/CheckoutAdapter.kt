package com.stepanusryan.bwamovie.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.model.Checkout
import java.text.NumberFormat
import java.util.*

class CheckoutAdapter(private var data: List<Checkout>,
                      private var listener: (Checkout) -> Unit)
    : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {
    lateinit var contextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView = layoutInflater.inflate(R.layout.row_item_checkout,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: CheckoutAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position],listener,contextAdapter)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txtSeat)
        private val harga: TextView = view.findViewById(R.id.txtHarga)
        private val image: ImageView = view.findViewById(R.id.posterImage)

        fun bindItem(data: Checkout, listener: (Checkout) -> Unit, context: Context)
        {
            val localId = Locale("in","ID")
            val formats = NumberFormat.getCurrencyInstance(localId)

            harga.text = formats.format(data.harga!!.toDouble())

            if (data.kursi !!.startsWith("Total"))
            {
                title.text = data.kursi
                title.setCompoundDrawables(null,null,null,null)
            }
            else
            {
                title.text = "Seat No. " + data.kursi
            }



            Glide.with(context)
                    .load(R.drawable.ic_event_seat_24px)
                    .into(image)

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}
