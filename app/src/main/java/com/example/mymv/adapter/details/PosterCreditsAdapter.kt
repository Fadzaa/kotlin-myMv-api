package com.example.mymv.adapter.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.ActorModel


class PosterCreditsAdapter (private val context: Context, private val castList: List<ActorModel> ):
    RecyclerView.Adapter<PosterCreditsAdapter.PosterViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterCreditsAdapter.PosterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.poster_credit_layout, parent, false)
        return PosterViewHolder(view)
    }

    override fun onBindViewHolder(holder: PosterCreditsAdapter.PosterViewHolder, position: Int) {
        val movieList = castList[position]
        println("Item at position $position: ${movieList.posterCredit}")
        holder.bind(movieList)
    }

    override fun getItemCount(): Int {
        return castList.size
    }


    inner class PosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var IMAGE_BASE = "https://image.tmdb.org/t/p/original/"
        private val movieImage: ImageView = itemView.findViewById(R.id.credit_poster)

        fun bind(movieList: ActorModel) {

            // Load actor image using Glide library
            Glide.with(context)
                .load(IMAGE_BASE + movieList.posterCredit)
                .into(movieImage)
        }
    }
}