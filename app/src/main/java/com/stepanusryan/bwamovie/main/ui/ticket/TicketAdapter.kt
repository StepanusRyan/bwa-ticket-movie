package com.stepanusryan.bwamovie.main.ui.ticket

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


class TicketAdapter(private var data: List<Checkout>,
                    private var listener: (Checkout) -> Unit)
    : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {
    lateinit var contextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView = layoutInflater.inflate(R.layout.row_item_checkout_white,parent,false)
        return TicketAdapter.ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: TicketAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position],listener,contextAdapter)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txtSeat)
        private val image: ImageView = view.findViewById(R.id.posterImage)

        fun bindItem(data: Checkout, listener: (Checkout) -> Unit, context: Context)
        {
            title.text = "Seat No. " + data.kursi
            Glide.with(context)
                .load(R.drawable.ic_event_seat_24px)
                .into(image)

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }


}
