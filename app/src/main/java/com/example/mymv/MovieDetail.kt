package com.example.mymv

import CastAdapter
import CastAdapter.OnItemClickListener
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.adapter.ListPosterAdapter
import com.example.mymv.adapter.details.RecommendationAdapter
import com.example.mymv.models.ActorModel
import com.example.mymv.models.CastResponseModel
import com.example.mymv.models.MovieModel
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.CreditsMovieInterface
import com.example.mymv.services.DetailsMovieInterface
import com.example.mymv.services.RecommendationMovieInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MovieDetail : AppCompatActivity(), CastAdapter.OnItemClickListener {


    private lateinit var listPosterAdapter: ListPosterAdapter
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/original/"
    private val BASE_URL = "https://api.themoviedb.org/3/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_movie)

        //PASSED ID
        val movieID = intent.getStringExtra("id")
        println(movieID)

        val movieIDNum = movieID!!.toInt()

        //BASE URL BUILDER
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //INTERFACE DETAILS
        val apiService: DetailsMovieInterface = retrofit.create(DetailsMovieInterface::class.java)


        //CALLBACK Function getMovie return Endpoint URL Details with Movie ID
        val call: Call<MovieModel> = apiService.getMovieDetails(movieIDNum)

        println(call)

        //CALL ENQUEUE DETAILS SECTION
        call.enqueue(object : Callback<MovieModel> {
            override fun onResponse(call: Call<MovieModel>, response: Response<MovieModel>) {
                println("This is the response "+  response.body())
                if (response.isSuccessful) {
                    val movieDetails: MovieModel? = response.body()
                    movieDetails?.let {
                        // Handle the movie details here
                        //Get Response
                        val title = it.title
                        val releaseDate = it.release
                        val overview = it.overview
                        val backdrop = it.backdropPath


//                        val genre: String = TextUtils.join(", ", genreMovie)

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


                        //Runtime Movie Format
                        val numRuntime = runTime!!.toInt()
                        val hours = numRuntime / 60
                        val remainingMinutes = numRuntime % 60
                        val runtimeFormat = String.format("%dh %02dm", hours, remainingMinutes)


                        //Vote Average Format


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



                        //Connecting ID from XML


                        // Set the values in the XML elements
                        movieTitle.text = title



                        //Set Response Api into XML
                        movieTitle.text = title
                        movieRuntime.text = runtimeFormat
                        movieOverview.text = overview
                        movieGenres.text = "$genreNames | "
                        movieVoteAverage.text = voteAverageFormat
                        movieVoteCount.text = String.format("(%s)", voteCount)


                        //Glide
//                            Glide.with(MovieDetails.this)
//                                    .load(IMAGE_BASE + posterPath)
//                                    .into(moviePoster);
                        Glide.with(this@MovieDetail)
                            .load(IMAGE_BASE + backdrop)
                            .into(movieBackdrop)
                    }
                }
                else {
                    // Handle error response
                    val errorCode = response.code()
                    val errorMessage = response.message()
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<MovieModel>, t: Throwable) {
                // Handle network failures or exceptions
                t.printStackTrace()
            }
        })



        //INTERFACE CREDITS
        val creditApiService: CreditsMovieInterface =
            retrofit.create(CreditsMovieInterface::class.java)

        val callCredits: Call<CastResponseModel> = creditApiService.getCreditDetails(movieIDNum)

        // CALL ENQUEUE CAST SECTION
        callCredits.enqueue(object : Callback<CastResponseModel> {
            override fun onResponse(
                call: Call<CastResponseModel>,
                response: Response<CastResponseModel>
            ) {
                if (response.isSuccessful) {
                    val castResponse: CastResponseModel? = response.body()
                    val castDetails: List<ActorModel>? = castResponse?.castList
                    castDetails?.let {
                        val rvCast = findViewById<RecyclerView>(R.id.rvCast)
                        val castAdapter = CastAdapter(applicationContext, castDetails, this@MovieDetail)
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
            }

            override fun onFailure(call: Call<CastResponseModel>, t: Throwable) {
                // Handle network failures or exceptions
                t.printStackTrace()
            }
        })

        //RECOMMENDATION INTERFACE
        val recommendationApiService: RecommendationMovieInterface = retrofit.create(RecommendationMovieInterface::class.java)

        val callRecommendation: Call<MovieResponse> = recommendationApiService.getRecommendationDetails(movieIDNum)

        callRecommendation.enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse: MovieResponse? = response.body()
                    val movieResults: List<MovieModel>? = movieResponse?.movieModels
                    movieResults?.let {
                        val rvMovieRecommendation = findViewById<RecyclerView>(R.id.rvRecomendations)
                        val recommendationAdapter = RecommendationAdapter(this@MovieDetail,movieResults)
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
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    override fun onItemClick(actorId: String) {
        val intent = Intent(this, ActorDetail::class.java)
        intent.putExtra("actorId", actorId)
        println("This is Actor ID : $actorId")
        startActivity(intent)
    }
}