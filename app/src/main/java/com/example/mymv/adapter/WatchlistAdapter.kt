package com.example.mymv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.MovieModel
import kotlinx.android.synthetic.main.movie_item.view.*

class WatchlistAdapter(private val movieModels: List<MovieModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<WatchlistAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindMovie(movieModel: MovieModel) {
            itemView.movie_title.text = movieModel.title
            itemView.release_date.text = movieModel.release
            Glide.with(itemView).load(IMAGE_BASE + movieModel.poster).into(itemView.movie_poster)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)

        return MovieViewHolder(itemView)
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