package com.example.mymv.adapter.seriesAdapter



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.TVModel

import kotlinx.android.synthetic.main.list_model.view.*

class ListPosterSeriesAdapter(private val tvModels: List<TVModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<ListPosterSeriesAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindMovie(tvModel: TVModel) {
            Glide.with(itemView).load(IMAGE_BASE + tvModel.poster).into(itemView.recomendation_poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_model, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(tvModels.get(position))
        val movie = tvModels[position]

        holder.itemView.setOnClickListener {
            listener.onClick(movie)
        }
    }

    override fun getItemCount(): Int = tvModels.size

    interface OnAdapterListener  {
        fun onClick(seriesModel : TVModel)
    }
}