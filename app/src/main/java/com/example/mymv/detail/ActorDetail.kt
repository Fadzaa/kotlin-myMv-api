package com.example.mymv.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.adapter.detailsMovieAdapter.ActorsCreditsAdapter
import com.example.mymv.models.ActorModel
import com.example.mymv.models.CastResponseModel
import com.example.mymv.services.ActorMovieCreditsInterface
import com.example.mymv.services.DetailsActorInterface
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ActorDetail : AppCompatActivity() {

    private val IMAGE_BASE = "https://image.tmdb.org/t/p/original/"
    private val BASE_URL = "https://api.themoviedb.org/3/"
    private lateinit var biographyText : TextView
    private lateinit var knownText : TextView

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor_detail)

        //PASSED ID
        val actorID = intent.getStringExtra("actorId")

        val actorIDNum = actorID!!.toInt()

        progressBar = findViewById(R.id.pbActorDetails)
        biographyText = findViewById(R.id.textBiography)
        knownText = findViewById(R.id.textKnown)

        biographyText.visibility = View.GONE
        knownText.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: DetailsActorInterface = retrofit.create(DetailsActorInterface::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val actorModel: ActorModel? = withContext(Dispatchers.IO) {
                    apiService.getActorDetails(actorIDNum).execute().body()
                }

                actorModel?.let {
                    val actorName: TextView = findViewById(R.id.tvActorName)
                    val actorImage: ImageView = findViewById(R.id.ivActor)
                    val actorBiography: TextView = findViewById(R.id.tvActorBiography)
                    val actorBirthday: TextView = findViewById(R.id.tvActorBirthday)
                    val actorGender: TextView = findViewById(R.id.tvActorGender)
                    val actorPlaceBirth: TextView = findViewById(R.id.tvActorPlace)

                    val getName = actorModel.actorName
                    val getImage = actorModel.actorImage
                    val getBiography = actorModel.actorBiography
                    val getBirthday = actorModel.actorBirthday
                    val getGender = actorModel.actorGender
                    val getPlaceBirth = actorModel.actorPlaceBirth

                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                    try {
                        val date = inputFormat.parse(getBirthday)
                        val outputDate = outputFormat.format(date)
                        println(outputDate)

                        actorBirthday.text = outputDate
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    val genderText = when (getGender) {
                        "1" -> "(Female)"
                        "2" -> "(Male)"
                        else -> ""
                    }

                    actorName.text = getName
                    actorBiography.text = getBiography
                    actorGender.text = genderText
                    actorPlaceBirth.text = getPlaceBirth

                    Glide.with(this@ActorDetail)
                        .load(IMAGE_BASE + getImage)
                        .into(actorImage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the error
            } finally {
                progressBar.visibility = View.GONE
                biographyText.visibility = View.VISIBLE
                knownText.visibility = View.VISIBLE
            }
        }

        val apiMovieCreditService: ActorMovieCreditsInterface =
            retrofit.create(ActorMovieCreditsInterface::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val movieResponse: CastResponseModel? = withContext(Dispatchers.IO) {
                    apiMovieCreditService.getActorMovieCredits(actorIDNum).execute().body()
                }

                val movieResults: List<ActorModel>? = movieResponse?.castList
                movieResults?.let {
                    val rvKnown = findViewById<RecyclerView>(R.id.rvKnownFor)
                    val creditAdapter = ActorsCreditsAdapter(this@ActorDetail, movieResults)
                    val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                        applicationContext, LinearLayoutManager.HORIZONTAL, false
                    )
                    rvKnown.layoutManager = mLayoutManager
                    rvKnown.adapter = creditAdapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the error
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
