package com.example.mymv.adapter.seriesAdapter



import android.content.Context
import android.view.*
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.TVModel
import kotlinx.android.synthetic.main.movie_item.view.*


class WatchlistSeriesAdapter(private var tvModelsMutable: MutableList<TVModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<WatchlistSeriesAdapter.TVViewHolder>(){



    class TVViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindMovie(tvModel: TVModel) {
            itemView.movie_title.text = tvModel.title
            itemView.release_date.text = tvModel.release
            Glide.with(itemView).load(IMAGE_BASE + tvModel.poster).into(itemView.movie_backdrop)
        }





    }

    fun updateData(updatedTvModels: MutableList<TVModel>?) {
        this.tvModelsMutable = updatedTvModels!!
        notifyDataSetChanged()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)



        return TVViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: TVViewHolder, position: Int) {
        holder.bindMovie(tvModelsMutable.get(position))
        val tv = tvModelsMutable!![position]


        holder.itemView.setOnClickListener {
            listener.onClick(tv)

        }

        holder.itemView.setOnLongClickListener { v ->
            val inflater = v.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val customDialogView = inflater.inflate(R.layout.action_sheet, null)

            val popupWindow = PopupWindow(customDialogView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

            popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0)


//            val deleteButton = customDialogView.findViewById<Button>(R.id.btn_delete)
//
//
//
//
//
//            deleteButton.setOnClickListener {
//                popupWindow.dismiss()
//                tvModelsMutable.removeAt(holder.adapterPosition)
//                notifyItemRemoved(holder.adapterPosition)
//
//                val builder = AlertDialog.Builder(v.context)
//
//
//                val alertDialog = builder.create()
//                alertDialog.show()
//
//            }
            true
        }








    }

    override fun getItemCount(): Int = tvModelsMutable!!.size


    interface OnAdapterListener  {
        fun onClick(tvModel : TVModel)
//        fun onLongClick(position: Int)
    }








}