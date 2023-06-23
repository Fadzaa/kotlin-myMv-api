package com.example.mymv.adapter

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.MovieModel
import kotlinx.android.synthetic.main.movie_item.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class WatchlistAdapter(private var movieModelsMutable: MutableList<MovieModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<WatchlistAdapter.TVViewHolder>(){


    interface OnAdapterListener  {
        fun onClick(movieId : String)
//        fun onLongClick(position: Int)
    }



    class TVViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindMovie(movieModel: MovieModel) {

            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            try {
                val date = inputFormat.parse(movieModel.release)
                val outputDate = outputFormat.format(date)
                println(outputDate)

                itemView.release_date.text = "Release Date : $outputDate"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val maxCharacterLimit = 120
            val originalText = movieModel.overview
            val truncatedText = if (originalText!!.length > maxCharacterLimit) {
                originalText!!.substring(0, maxCharacterLimit) + "..."
            } else {
                originalText
            }


            itemView.movie_title.text = movieModel.title

            itemView.movie_overview.text = truncatedText
            Glide.with(itemView).load(IMAGE_BASE + movieModel.poster).into(itemView.movie_backdrop)
        }


    }

    fun updateData(updatedMovieModels: MutableList<MovieModel>?) {
        this.movieModelsMutable = updatedMovieModels!!
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)



        return TVViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: TVViewHolder, position: Int) {
        holder.bindMovie(movieModelsMutable.get(position))
        val movie = movieModelsMutable!![position]


        holder.itemView.setOnClickListener {
            listener.onClick(movie.id!!)


        }

//        holder.itemView.setOnLongClickListener { v ->
//            val inflater = v.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val customDialogView = inflater.inflate(R.layout.action_sheet, null)
//
//            val popupWindow = PopupWindow(customDialogView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
//
//            popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0)
//
//
//            val deleteButton = customDialogView.findViewById<Button>(R.id.btn_delete)
//
//
//
//
//
//            deleteButton.setOnClickListener {
//                popupWindow.dismiss()
//                movieModelsMutable.removeAt(holder.adapterPosition)
//                notifyItemRemoved(holder.adapterPosition)
//
//                val builder = AlertDialog.Builder(v.context)
//
//
//                val alertDialog = builder.create()
//                alertDialog.show()
//
//            }
//            true
//        }








    }

    override fun getItemCount(): Int = movieModelsMutable!!.size










}