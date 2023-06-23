package com.example.mymv.fragments


import TrailerAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mymv.ListGenreMovie
import com.example.mymv.detail.MovieDetail
import com.example.mymv.R
import com.example.mymv.SearchBar
import com.example.mymv.adapter.homeAdapter.HomePosterAdapter
import com.example.mymv.adapter.homeAdapter.ListPosterAdapter
import com.example.mymv.adapter.homeAdapter.ListGenreAdapter

import com.example.mymv.models.*
import com.example.mymv.services.*
import com.example.mymv.services.movieInterface.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home_fragments.*
import kotlinx.android.synthetic.main.fragment_home_fragments.rvListGenre
import kotlinx.android.synthetic.main.fragment_series.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragments : Fragment() {

    private lateinit var homePosterAdapter: HomePosterAdapter
    private lateinit var listPosterAdapter: ListPosterAdapter
    private lateinit var listGenreAdapter: ListGenreAdapter
    private lateinit var searchBar: Button
    private lateinit var rvTrailers:  RecyclerView
    private lateinit var userNameText: TextView

    private val firestoreDB = FirebaseFirestore.getInstance()

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
    : View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home_fragments, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBar = view.findViewById(R.id.search_bar)

        searchBar.setOnClickListener{
            startActivity(Intent(requireContext(), SearchBar::class.java))
        }

        userNameText = view.findViewById(R.id.userNameText)
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val providerData = currentUser.providerData
            if (providerData.isEmpty()) {
                val displayName = currentUser.displayName
                userNameText.text = displayName
            } else {
                // User is not anonymous
                // Perform actions for authenticated user
            }
        } else {
            userNameText.text = "Guest"
        }






//        rvTrailers = view.findViewById(R.id.rv_new_trailer)

//        lifecycleScope.launch {
//            val movieId = MovieIDClient.getNowPlayingMovieIds()
//            fetchMovieTrailers(movieId = movieId.toString())
//        }

//        trailerAdapter = TrailerAdapter(mutableListOf())
//        rvTrailers.layoutManager = LinearLayoutManager(requireContext())
//        rvTrailers.adapter = trailerAdapter

        val snapHelper1 = PagerSnapHelper()
        val snapHelper2 = PagerSnapHelper()
        val snapHelper3 = PagerSnapHelper()
        val snapHelper4 = PagerSnapHelper()



        homePosterAdapter = HomePosterAdapter(mutableListOf(), object: HomePosterAdapter.OnAdapterListener{
            override fun onClick(movieModel: MovieModel) {
                Log.d("ErrorCheck", "This is Log Tap")
                startActivity(Intent(requireContext() , MovieDetail::class.java)
                    .putExtra("id", movieModel.id)
                )

            }

        })

        listPosterAdapter = ListPosterAdapter(mutableListOf(), object: ListPosterAdapter.OnAdapterListener{
            override fun onClick(movieModel: MovieModel) {
                startActivity(Intent(requireContext() , MovieDetail::class.java)
                    .putExtra("id", movieModel.id)
                )
            }

        })

        listGenreAdapter = ListGenreAdapter(mutableListOf(), object: ListGenreAdapter.OnAdapterListener{
            override fun onClick(genreModel: GenreModel) {

                Log.d("ErrorCheck", "This is ${genreModel.name}")
                startActivity(Intent(requireContext() , ListGenreMovie::class.java)
                    .putExtra("genre_id", genreModel.id)
                    .putExtra("genre_name", genreModel.name)
                )


            }

        })

//        val movieIds = MovieIDClient.getNowPlayingMovieIds()

        snapHelper1.attachToRecyclerView(rv_in_theatre)
        rvListGenre.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvListGenre.setHasFixedSize(true)
        getListGenre{ genreModels : List<GenreModel> ->
            rvListGenre.adapter = ListGenreAdapter(genreModels, listGenreAdapter.listener)
        }



// Set the SnapHelper to the RecyclerView

        snapHelper2.attachToRecyclerView(rvListGenre)

        rv_in_theatre.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_in_theatre.setHasFixedSize(true)
        getMovieData { movieModels: List<MovieModel> ->
            rv_in_theatre.adapter = HomePosterAdapter(movieModels, homePosterAdapter.listener)
            startTimer()
        }

        snapHelper3.attachToRecyclerView(top_rated_movie_list)
        top_rated_movie_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        top_rated_movie_list.setHasFixedSize(true)
        getTopRatedMovieData { movieModels : List<MovieModel> ->
            top_rated_movie_list.adapter = ListPosterAdapter(movieModels, listPosterAdapter.listener)
        }


        snapHelper4.attachToRecyclerView(rv_trending_movies)
        rv_trending_movies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_trending_movies.setHasFixedSize(true)
        getTrendingMovieData { movieModels : List<MovieModel> ->
            rv_trending_movies.adapter = ListPosterAdapter(movieModels, listPosterAdapter.listener)
        }
    }


    private fun getListGenre(callback: (List<GenreModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(ListGenreInterface::class.java)
        apiService.getGenreList().enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                return callback(response.body()!!.genreModels)
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {

            }

        })
    }

    private fun getMovieData(callback: (List<MovieModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(MoviePopularInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movieModels)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }

    private fun getTopRatedMovieData(callback: (List<MovieModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(TopRatedMovie::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movieModels)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }


    private fun getTrendingMovieData(callback: (List<MovieModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(TrendingMovieInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movieModels)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }

//    private fun fetchMovieTrailers(movieId: String) {
//        runBlocking {
//            val trailers = MovieIDClient.getMovieTrailers(movieId)
//            trailerAdapter.updateTrailers(trailers)
//        }
//    }

    //Settings Timer for RecyclerView
    private fun startTimer() {
        val interval = 4000L

        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    val layoutManager = rv_in_theatre.layoutManager as? LinearLayoutManager
                    layoutManager?.let {
                        val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                        val nextPosition = if (currentPosition == RecyclerView.NO_POSITION || currentPosition == layoutManager.itemCount - 1)
                            0
                        else
                            currentPosition + 1
                        rv_in_theatre.smoothScrollToPosition(nextPosition)
                    }
                }
            }
        }

        // Schedule the TimerTask to run at the specified interval, repeatedly
        timer?.scheduleAtFixedRate(timerTask, interval, interval)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the Timer and TimerTask
        timer?.cancel()
        timerTask?.cancel()
    }





}