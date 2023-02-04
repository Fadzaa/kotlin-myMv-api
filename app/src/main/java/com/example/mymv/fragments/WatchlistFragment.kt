package com.example.mymv.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymv.MovieDetail
import com.example.mymv.R
import com.example.mymv.adapter.WatchlistAdapter
import com.example.mymv.models.Movie
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.MovieApiService
import com.example.mymv.services.UpcomingMoviesInterface
import kotlinx.android.synthetic.main.fragment_watchlist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WatchlistFragment : Fragment() {

    private lateinit var watchlistAdapter: WatchlistAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_watchlist, container, false)


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        watchlistAdapter = WatchlistAdapter(mutableListOf(), object: WatchlistAdapter.OnAdapterListener{
            override fun onClick(movie: Movie) {
                startActivity(Intent(requireContext() , MovieDetail::class.java)
                    .putExtra("title_movie", movie.title)
                    .putExtra("poster_movie", movie.poster)
                    .putExtra("movie_release", movie.release)
                    .putExtra("backdrop_path", movie.backdropPath)
                    .putExtra("movie_overview", movie.overview)
                )
            }

        })
        upcoming_movie_list.layoutManager = LinearLayoutManager(requireContext())
        upcoming_movie_list.setHasFixedSize(true)
        getMovieData { movies : List<Movie> ->
            upcoming_movie_list.adapter = WatchlistAdapter(movies, watchlistAdapter.listener)
        }





    }



    private fun getMovieData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(UpcomingMoviesInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }



    }