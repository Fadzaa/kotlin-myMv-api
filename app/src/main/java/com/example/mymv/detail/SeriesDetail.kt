package com.example.mymv.detail

import CastAdapter
import android.content.Intent
import android.graphics.Color
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
import com.example.mymv.adapter.detailsMovieAdapter.RecommendationAdapter
import com.example.mymv.adapter.detailsSeries.RecommendationAdapterSeries
import com.example.mymv.models.*
import com.example.mymv.services.CreditsMovieInterface
import com.example.mymv.services.detailsInterface.DetailsSeriesInterface
import com.example.mymv.services.detailsInterface.RecommendationSeriesInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*

class SeriesDetail : AppCompatActivity(), CastAdapter.OnItemClickListener, RecommendationAdapterSeries.OnRecommendationSelected {

    private lateinit var arrowButton : ImageView
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/original/"
    private val BASE_URL = "https://api.themoviedb.org/3/"
    private lateinit var summaryText : TextView
    private lateinit var castText : TextView
    private lateinit var similarText : TextView

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series_detail)

        arrowButton = findViewById(R.id.back_arrow)
        progressBar = findViewById(R.id.pbSeriesDetails)
        summaryText = findViewById(R.id.summary)
        castText = findViewById(R.id.cast)
        similarText = findViewById(R.id.similar_movie)

        arrowButton.setOnClickListener {
            finish()
        }

        //PASSED ID
        val seriesID = intent.getStringExtra("tv_id")
        println(seriesID)

        val movieIDNum = seriesID!!.toInt()

        //BASE URL BUILDER
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //INTERFACE DETAILS
        val apiService: DetailsSeriesInterface = retrofit.create(DetailsSeriesInterface::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            progressBar.visibility = View.VISIBLE

            try {
                val response: Response<TVModel> = withContext(Dispatchers.IO) {
                    apiService.getSeriesDetails(movieIDNum).execute()
                }

                if (response.isSuccessful) {
                    val movieDetails: TVModel? = response.body()
                    movieDetails?.let {
                        // Handle the movie details here
                        //Get Response
                        val title = it.title
                        val releaseDate = it.release
                        val overview = it.overview
                        val backdrop = it.backdropPath

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

                        //Format method using SimpleDateFormat to change format date from '2023 05 20 into 20 May 2023'
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                        try {
                            val date = inputFormat.parse(releaseDate)
                            val outputDate = outputFormat.format(date)
                            println(outputDate)

                            movieRelease.text = "Release Date : $outputDate"
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }

                        //Vote Average Format
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

                        //Set Response Api into XML
                        movieTitle.text = title
                        movieOverview.text = overview
                        movieGenres.text = "$genreNames | "
                        movieVoteAverage.text = voteAverageFormat
                        movieVoteCount.text = String.format("(%s)", voteCount)

                        //Glide
                        Glide.with(this@SeriesDetail)
                            .load(IMAGE_BASE + backdrop)
                            .into(movieBackdrop)
                    }
                } else {
                    // Handle error response
                    val errorCode = response.code()
                    val errorMessage = response.message()
                    // Handle the error
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the error
            }

            progressBar.visibility = View.GONE
            summaryText.visibility = View.VISIBLE
            castText .visibility = View.VISIBLE
            similarText.visibility = View.VISIBLE
        }

        //INTERFACE CREDITS
        val creditApiService: CreditsMovieInterface =
            retrofit.create(CreditsMovieInterface::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            progressBar.visibility = View.VISIBLE

            try {
                val response: Response<CastResponseModel> = withContext(Dispatchers.IO) {
                    creditApiService.getCreditDetails(movieIDNum).execute()
                }

                if (response.isSuccessful) {
                    val castResponse: CastResponseModel? = response.body()
                    val castDetails: List<ActorModel>? = castResponse?.castList
                    castDetails?.let {
                        val rvCast = findViewById<RecyclerView>(R.id.rvCast)
                        val castAdapter = CastAdapter(applicationContext, castDetails, this@SeriesDetail)
                        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                            applicationContext, LinearLayoutManager.HORIZONTAL, false
                        )
                        rvCast.layoutManager = mLayoutManager
                        rvCast.adapter = castAdapter
                    }
                } else {
                    // Handle error response
                    val errorCode = response.code()
                    val errorMessage = response.message()
                    // Handle the error
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the error
            }

            progressBar.visibility = View.GONE
        }

        //RECOMMENDATION INTERFACE
        val recommendationApiService: RecommendationSeriesInterface = retrofit.create(
            RecommendationSeriesInterface::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            progressBar.visibility = View.VISIBLE

            try {
                val response: Response<TVResponse> = withContext(Dispatchers.IO) {
                    recommendationApiService.getRecommendationDetails(movieIDNum).execute()
                }

                if (response.isSuccessful) {
                    val tvResponse: TVResponse? = response.body()
                    val tvResults: List<TVModel>? = tvResponse?.tvModels
                    tvResults?.let {
                        val rvMovieRecommendation = findViewById<RecyclerView>(R.id.rvRecomendations)
                        val recommendationAdapter = RecommendationAdapterSeries(this@SeriesDetail,tvResults, this@SeriesDetail)
                        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                            applicationContext, LinearLayoutManager.HORIZONTAL, false
                        )
                        rvMovieRecommendation.layoutManager = mLayoutManager
                        rvMovieRecommendation.adapter = recommendationAdapter
                    }
                } else {
                    // Handle error response
                    val errorCode = response.code()
                    val errorMessage = response.message()
                    // Handle the error
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the error
            }

            progressBar.visibility = View.GONE
        }
    }

    override fun onItemClick(actorId: String) {
        val intent = Intent(this, ActorDetail::class.java)
        intent.putExtra("actorId", actorId)
        println("This is Actor ID : $actorId")
        startActivity(intent)
    }

    override fun onItemSelected(movieid: String) {
        val intent = Intent(this, SeriesDetail::class.java)
        intent.putExtra("tv_id", movieid)
        println("This is Actor ID : $movieid")
        startActivity(intent)
        finish()
    }
}
