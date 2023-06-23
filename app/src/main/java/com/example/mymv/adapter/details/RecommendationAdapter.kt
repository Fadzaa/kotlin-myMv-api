package com.example.mymv.adapter.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.MovieModel

class RecommendationAdapter(private val context: Context, private val movieModels: List<MovieModel> ) :
    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_model, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val movieList = movieModels[position]
        holder.bind(movieList)
    }

    override fun getItemCount(): Int {
        return movieModels.size
    }

    inner class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var IMAGE_BASE = "https://image.tmdb.org/t/p/original/"
        private val movieImage: ImageView = itemView.findViewById(R.id.recomendation_poster)

        fun bind(movieList: MovieModel) {

            // Load actor image using Glide library
            Glide.with(context)
                .load(IMAGE_BASE + movieList.poster)
                .into(movieImage)
        }
    }


}