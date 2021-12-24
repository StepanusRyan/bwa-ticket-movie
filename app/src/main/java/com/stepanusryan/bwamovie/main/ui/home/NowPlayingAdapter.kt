package com.stepanusryan.bwamovie.main.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.model.Film

class NowPlayingAdapter(private var data: List<Film>,
                        private var listener: (Film) -> Unit)
    : RecyclerView.Adapter<NowPlayingAdapter.ViewHolder>(){

    lateinit var contextAdapter:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView = layoutInflater.inflate(R.layout.row_item_now_playing,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: NowPlayingAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position],listener,contextAdapter)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view :View):RecyclerView.ViewHolder(view) {
        private val title:TextView = view.findViewById(R.id.txtTitleMov)
        private val genre:TextView = view.findViewById(R.id.txtGenreMov)
        private val rate:TextView = view.findViewById(R.id.txtRateMov)
        private val image:ImageView = view.findViewById(R.id.posterImage)

        fun bindItem(data:Film, listener: (Film) -> Unit, context: Context)
        {
            title.text = data.judul
            genre.text = data.genre
            rate.text = data.rating

            Glide.with(context)
                    .load(data.poster)
                    .into(image)

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}
