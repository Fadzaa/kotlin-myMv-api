package com.example.mymv.adapter.detailsSeries

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.MovieModel
import com.example.mymv.models.TVModel

class RecommendationAdapterSeries(private val context: Context, private val tvModels: List<TVModel>, private val listener: OnRecommendationSelected) :
    RecyclerView.Adapter<RecommendationAdapterSeries.RecommendationViewHolder>() {

    interface OnRecommendationSelected {
        fun onItemSelected(movieID: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_model, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val movieList = tvModels[position]
        holder.bind(movieList)

        holder.itemView.setOnClickListener{
            listener.onItemSelected(movieList.id!!)
        }
    }

    override fun getItemCount(): Int {
        return tvModels.size
    }

    inner class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var IMAGE_BASE = "https://image.tmdb.org/t/p/original/"
        private val movieImage: ImageView = itemView.findViewById(R.id.recomendation_poster)

        fun bind(movieList: TVModel) {

            // Load actor image using Glide library
            Glide.with(context)
                .load(IMAGE_BASE + movieList.poster)
                .into(movieImage)
        }
    }


}