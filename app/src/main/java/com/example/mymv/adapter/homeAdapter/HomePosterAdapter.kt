package com.example.mymv.adapter.homeAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.MovieModel
import kotlinx.android.synthetic.main.movie_item.view.*

class HomePosterAdapter(private val movieModels: List<MovieModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<HomePosterAdapter.MovieViewHolder>() {
    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/original/"

        fun bindMovie(movieModel: MovieModel) {


            Glide.with(itemView).load(IMAGE_BASE + movieModel.poster).into(itemView.movie_backdrop)
            itemView.movie_title.text = movieModel.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_poster_model, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movieModels.get(position))
        val movies = movieModels[position]

        holder.itemView.setOnClickListener {
            listener.onClick(movies)
        }
    }

    override fun getItemCount(): Int = movieModels.size

    interface OnAdapterListener  {
        fun onClick(movieModel : MovieModel)
    }
}