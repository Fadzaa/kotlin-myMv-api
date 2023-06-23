import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import kotlinx.android.synthetic.main.trailer_model.view.*

class TrailerAdapter(private val trailerUrls: MutableList<String>) :
    RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {

    class TrailerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val thumbnailImageView: ImageView = view.findViewById(R.id.ivThumbnailTrailer)

        fun bindTrailer(trailerUrl: String) {
            val thumbnailUrl = getThumbnailUrl(trailerUrl)

            Glide.with(itemView.context)
                .load(thumbnailUrl)
                .into(thumbnailImageView)


        }

        private fun getThumbnailUrl(trailerUrl: String): String {
            // Extract the trailer key from the URL
            val trailerKey = trailerUrl.substringAfterLast("=")

            // Return the thumbnail URL using the desired size option
            return "https://i.ytimg.com/vi/$trailerKey/default.jpg"
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        return TrailerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.trailer_model, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailerUrl = trailerUrls[position]

        // Load thumbnail image using a library like Glide or Picasso
        Glide.with(holder.itemView.context)
            .load(getThumbnailUrl(trailerUrl))
            .into(holder.itemView.ivThumbnailTrailer)

        holder.itemView.ivThumbnailTrailer.setOnClickListener {
            // Handle thumbnail click event
            openYouTubeVideo(trailerUrl)
        }




    }

    override fun getItemCount(): Int = trailerUrls.size

    private fun getThumbnailUrl(trailerUrl: String): String {
        // Extract the trailer key from the URL
        val trailerKey = trailerUrl.substringAfterLast("=")

        // Return the thumbnail URL using the desired size option
        return "https://i.ytimg.com/vi/$trailerKey/default.jpg"
    }

    private fun openYouTubeVideo(trailerUrl: String) {
        // Extract the trailer key from the URL
        val trailerKey = trailerUrl.substringAfterLast("=")

        // Construct the YouTube URL for the trailer
        val youTubeUrl = "https://www.youtube.com/watch?v=$trailerKey"

        // Open the YouTube video using an appropriate method (e.g., Intent or WebView)
        // ...
    }


    fun updateTrailers(newTrailerUrls: List<String>) {
        trailerUrls.clear()
        trailerUrls.addAll(newTrailerUrls)
        notifyDataSetChanged()
    }
}
