package com.stepanusryan.bwamovie.detailmovie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.model.Plays

class PlaysAdapter(private var data: List<Plays>,
                   private var listener: (Plays) -> Unit)
    : RecyclerView.Adapter<PlaysAdapter.ViewHolder>(){

    lateinit var contextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaysAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView = layoutInflater.inflate(R.layout.row_item_plays,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: PlaysAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position],listener,contextAdapter)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.posterImage)
        private val actor: TextView = view.findViewById(R.id.txtActor)

        fun bindItem(data:Plays, listener: (Plays) -> Unit, context: Context)
        {
            actor.text = data.nama
            Glide.with(context)
                    .load(data.url)
                    .into(image)
            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}


