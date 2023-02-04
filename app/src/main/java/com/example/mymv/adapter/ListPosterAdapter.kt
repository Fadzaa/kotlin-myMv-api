package com.example.mymv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.Movie
import kotlinx.android.synthetic.main.list_model.view.*

class ListPosterAdapter(private val movies: List<Movie>, val listener: ListPosterAdapter.OnAdapterListener) :
    RecyclerView.Adapter<ListPosterAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindMovie(movie: Movie) {
            Glide.with(itemView).load(IMAGE_BASE + movie.poster).into(itemView.list_poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_model, parent, false)
        )
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