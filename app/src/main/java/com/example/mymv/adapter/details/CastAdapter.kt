import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.models.ActorModel


class CastAdapter(private val context: Context, private val castList: List<ActorModel>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(actorId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_layout, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castMember = castList[position]
        holder.bind(castMember)

        holder.itemView.setOnClickListener{
            listener.onItemClick(castMember.id!!)
        }
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var IMAGE_BASE = "https://image.tmdb.org/t/p/original/"
        private val actorNameTextView: TextView = itemView.findViewById(R.id.tv_actor_name)
        private val actorImageView: ImageView = itemView.findViewById(R.id.cast_imageview)

        fun bind(castMember: ActorModel) {
            actorNameTextView.text = castMember.actorName

            // Load actor image using Glide library
            Glide.with(context)
                .load(IMAGE_BASE + castMember.actorImage)
                .into(actorImageView)
        }
    }
}
