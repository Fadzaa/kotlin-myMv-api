package com.example.mymv.detail

import CastAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.R
import com.example.mymv.adapter.detailsMovieAdapter.RecommendationAdapter
import com.example.mymv.models.ActorModel
import com.example.mymv.models.CastResponseModel
import com.example.mymv.models.MovieModel
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.CreditsMovieInterface
import com.example.mymv.services.DetailsMovieInterface
import com.example.mymv.services.RecommendationMovieInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MovieDetail : AppCompatActivity(), CastAdapter.OnItemClickListener, RecommendationAdapter.OnRecommendationSelected {

    private lateinit var arrowButton: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var summaryText : TextView
    private lateinit var castText : TextView
    private lateinit var similarText : TextView

    private val IMAGE_BASE = "https://image.tmdb.org/t/p/original/"
    private val BASE_URL = "https://api.themoviedb.org/3/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_movie)

        arrowButton = findViewById(R.id.back_arrow)
        progressBar = findViewById(R.id.pbMovieDetails)
        summaryText = findViewById(R.id.summary)
        castText = findViewById(R.id.cast)
        similarText = findViewById(R.id.similar_movie)
        progressBar.visibility = View.VISIBLE

        arrowButton.setOnClickListener {
            finish()
        }

        // PASSED ID
        val movieID = intent.getStringExtra("id")
        println(movieID)

        val movieIDNum = movieID!!.toInt()

        // BASE URL BUILDER
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // INTERFACE DETAILS
        val apiService: DetailsMovieInterface = retrofit.create(DetailsMovieInterface::class.java)

        // Coroutine code to fetch movie details
        GlobalScope.launch(Dispatchers.Main) {
            val movieDetails: MovieModel? = withContext(Dispatchers.IO) {
                val call = apiService.getMovieDetails(movieIDNum)
                val response = call.execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            }

            movieDetails?.let {
                // Handle the movie details here
                // Get Response
                val title = it.title
                val releaseDate = it.release
                val overview = it.overview
                val backdrop = it.backdropPath
                val runTime = it.runtime
                val voteAvg = it.voteAverage
                val voteCount = it.voteCount

                val genreList = movieDetails.genres
                val genreNames = genreList?.joinToString(", ") { it.name }

                val movieTitle = findViewById<TextView>(R.id.movie_title_details)
                val movieRelease = findViewById<TextView>(R.id.movie_release_details)
                val movieOverview = findViewById<TextView>(R.id.movie_overview_details)
                val arrowBack = findViewById<ImageView>(R.id.back_arrow)
                val movieBackdrop = findViewById<ImageView>(R.id.backdrop_path)
                val movieGenres = findViewById<TextView>(R.id.movie_genre_details)
                val movieRuntime = findViewById<TextView>(R.id.movie_runtime_details)
                val movieVoteAverage = findViewById<TextView>(R.id.movie_vote_average)
                val movieVoteCount = findViewById<TextView>(R.id.movie_vote_count)

                // Format method using SimpleDateFormat to change format date from '2023 05 20 into 20 May 2023'
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                try {
                    val date = inputFormat.parse(releaseDate)
                    val outputDate = outputFormat.format(date)
                    println(outputDate)

                    movieRelease.text = "Release Date: $outputDate"
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                // Runtime Movie Format
                val numRuntime = runTime!!.toInt()
                val hours = numRuntime / 60
                val remainingMinutes = numRuntime % 60
                val runtimeFormat = String.format("%dh %02dm", hours, remainingMinutes)

                // Vote Average Format
                val numVoteAvg = voteAvg!!.toDouble()

                if (numVoteAvg < 7) {
                    movieVoteAverage.setTextColor(Color.parseColor("#FA4A3E"))
                } else if (numVoteAvg < 8) {
                    movieVoteAverage.setTextColor(Color.parseColor("#FFEF5A"))
                } else {
                    movieVoteAverage.setTextColor(Color.parseColor("#49F28D"))
                }

                val voteAverageFormat = String.format(Locale.US, "%.1f", numVoteAvg)

                // Set the values in the XML elements
                movieTitle.text = title
                movieRuntime.text = runtimeFormat
                movieOverview.text = overview
                movieGenres.text = "$genreNames | "
                movieVoteAverage.text = voteAverageFormat
                movieVoteCount.text = String.format("(%s)", voteCount)

                // Glide
                Glide.with(this@MovieDetail)
                    .load(IMAGE_BASE + backdrop)
                    .into(movieBackdrop)
            }

            progressBar.visibility = View.GONE
            summaryText.visibility = View.VISIBLE
            castText .visibility = View.VISIBLE
            similarText.visibility = View.VISIBLE
        }

        // INTERFACE CREDITS
        val creditApiService: CreditsMovieInterface =
            retrofit.create(CreditsMovieInterface::class.java)

        // Coroutine code to fetch cast details
        GlobalScope.launch(Dispatchers.Main) {
            val castDetails: List<ActorModel>? = withContext(Dispatchers.IO) {
                val callCredits = creditApiService.getCreditDetails(movieIDNum)
                val response = callCredits.execute()
                if (response.isSuccessful) {
                    response.body()?.castList
                } else {
                    null
                }
            }

            castDetails?.let {
                val rvCast = findViewById<RecyclerView>(R.id.rvCast)
                val castAdapter = CastAdapter(applicationContext, castDetails, this@MovieDetail)
                val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                    applicationContext, LinearLayoutManager.HORIZONTAL, false
                )
                rvCast.layoutManager = mLayoutManager
                rvCast.adapter = castAdapter
            }
        }

        // RECOMMENDATION INTERFACE
        val recommendationApiService: RecommendationMovieInterface =
            retrofit.create(RecommendationMovieInterface::class.java)

        // Coroutine code to fetch recommendation details
        GlobalScope.launch(Dispatchers.Main) {
            val movieResults: List<MovieModel>? = withContext(Dispatchers.IO) {
                val callRecommendation = recommendationApiService.getRecommendationDetails(movieIDNum)
                val response = callRecommendation.execute()
                if (response.isSuccessful) {
                    response.body()?.movieModels
                } else {
                    null
                }
            }

            movieResults?.let {
                val rvMovieRecommendation = findViewById<RecyclerView>(R.id.rvRecomendations)
                val recommendationAdapter = RecommendationAdapter(this@MovieDetail, movieResults, this@MovieDetail)
                val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                    applicationContext, LinearLayoutManager.HORIZONTAL, false
                )
                rvMovieRecommendation.layoutManager = mLayoutManager
                rvMovieRecommendation.adapter = recommendationAdapter
            }
        }
    }

    override fun onItemClick(actorId: String) {
        val intent = Intent(this, ActorDetail::class.java)
        intent.putExtra("actorId", actorId)
        println("This is Actor ID : $actorId")
        startActivity(intent)
    }

    override fun onItemSelected(movieid: String) {
        val intent = Intent(this, MovieDetail::class.java)
        intent.putExtra("id", movieid)
        println("This is Actor ID : $movieid")
        startActivity(intent)
        finish()
    }
}



