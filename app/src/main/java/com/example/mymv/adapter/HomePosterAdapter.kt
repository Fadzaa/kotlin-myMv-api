package com.example.mymv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.Movie
import kotlinx.android.synthetic.main.movie_item.view.*

class HomePosterAdapter(private val movies: List<Movie>, val listener: HomePosterAdapter.OnAdapterListener) :
    RecyclerView.Adapter<HomePosterAdapter.MovieViewHolder>() {
    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindMovie(movie: Movie) {
            Glide.with(itemView).load(IMAGE_BASE + movie.poster).into(itemView.movie_poster)
            itemView.movie_title.text = movie.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_poster_model, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movies.get(position))
        val movies = movies[position]

        holder.itemView.setOnClickListener {
            listener.onClick(movies)
        }
    }

    override fun getItemCount(): Int = movies.size

    interface OnAdapterListener  {
        fun onClick(movie : Movie)
    }
}