package com.example.mymv.adapter.homeAdapter



import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.mymv.R
import com.example.mymv.models.GenreModel
import kotlinx.android.synthetic.main.genre_button.view.*

class ListGenreAdapter(private val genreModels: List<GenreModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<ListGenreAdapter.GenreViewHolder>() {

    private lateinit var btnGenre: Button
    class GenreViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bindGenre(genreModel: GenreModel) {
            itemView.btnGenre.text = genreModel.name


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.genre_button, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bindGenre(genreModels[position])
        val genres = genreModels[position]

        holder.itemView.setOnClickListener {
            listener.onClick(genres)
        }
    }

    override fun getItemCount(): Int = genreModels.size

    interface OnAdapterListener  {
        fun onClick(genreModel: GenreModel )
    }
}