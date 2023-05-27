package com.example.mymv.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.MovieModel
import kotlinx.android.synthetic.main.movie_item.view.*


class WatchlistAdapter(private var movieModelsMutable: MutableList<MovieModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<WatchlistAdapter.MovieViewHolder>(){



    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindMovie(movieModel: MovieModel) {
            itemView.movie_title.text = movieModel.title
            itemView.release_date.text = movieModel.release
            Glide.with(itemView).load(IMAGE_BASE + movieModel.poster).into(itemView.movie_backdrop)
        }





    }

    fun updateData(updatedMovieModels: MutableList<MovieModel>?) {
        this.movieModelsMutable = updatedMovieModels!!
        notifyDataSetChanged()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)



        return MovieViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movieModelsMutable.get(position))
        val movie = movieModelsMutable!![position]


        holder.itemView.setOnClickListener {
            listener.onClick(movie)

        }

        holder.itemView.setOnLongClickListener { v ->
            val inflater = v.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val customDialogView = inflater.inflate(R.layout.action_sheet, null)

            val popupWindow = PopupWindow(customDialogView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

            popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0)


            val deleteButton = customDialogView.findViewById<Button>(R.id.btn_delete)





            deleteButton.setOnClickListener {
                popupWindow.dismiss()
                movieModelsMutable.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)

                val builder = AlertDialog.Builder(v.context)


                val alertDialog = builder.create()
                alertDialog.show()

            }
            true
        }








    }

    override fun getItemCount(): Int = movieModelsMutable!!.size


    interface OnAdapterListener  {
        fun onClick(movieModel : MovieModel)
//        fun onLongClick(position: Int)
    }








}