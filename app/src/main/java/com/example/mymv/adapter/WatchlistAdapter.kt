package com.example.mymv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.Movie
import kotlinx.android.synthetic.main.movie_item.view.*

class WatchlistAdapter(private val movies: List<Movie>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<WatchlistAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindMovie(movie: Movie) {
            itemView.movie_title.text = movie.title
            itemView.release_date.text = movie.release
            Glide.with(itemView).load(IMAGE_BASE + movie.poster).into(itemView.movie_poster)
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)

        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movies.get(position))
        val movie = movies[position]

        holder.itemView.setOnClickListener {
            listener.onClick(movie)
        }
    }

    override fun getItemCount(): Int = movies.size


    interface OnAdapterListener  {
        fun onClick(movie : Movie)
    }
}