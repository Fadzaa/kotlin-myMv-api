package com.example.mymv.adapter.seriesAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.MovieModel
import com.example.mymv.models.TVModel
import kotlinx.android.synthetic.main.movie_item.view.*

class OnAirPosterAdapter(private val tvModels: List<TVModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<OnAirPosterAdapter.SeriesViewHolder>() {

    class SeriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/original/"

        fun bindMovie(seriesModel: TVModel) {


            Glide.with(itemView).load(IMAGE_BASE + seriesModel.poster).into(itemView.movie_backdrop)
            itemView.movie_title.text = seriesModel.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_poster_model, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bindMovie(tvModels.get(position))
        val tv = tvModels[position]

        holder.itemView.setOnClickListener {
            listener.onClick(tv)
        }
    }

    override fun getItemCount(): Int = tvModels.size

    interface OnAdapterListener  {
        fun onClick(tvModel : TVModel)
    }
}