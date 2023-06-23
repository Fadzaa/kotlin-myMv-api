package com.example.mymv.adapter.homeAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.MovieModel
import kotlinx.android.synthetic.main.list_model.view.*

class ListPosterAdapter(private val movieModels: List<MovieModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<ListPosterAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindMovie(movieModel: MovieModel) {
            Glide.with(itemView).load(IMAGE_BASE + movieModel.poster).into(itemView.recomendation_poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_model, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movieModels.get(position))
        val movie = movieModels[position]

        holder.itemView.setOnClickListener {
            listener.onClick(movie)
        }
    }

    override fun getItemCount(): Int = movieModels.size

    interface OnAdapterListener  {
        fun onClick(movieModel : MovieModel)
    }
}