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
import com.example.mymv.adapter.HomePosterAdapter
import com.example.mymv.adapter.ListPosterAdapter
import com.example.mymv.models.Movie
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.LatestMovieInterface
import com.example.mymv.services.MovieApiInterface
import com.example.mymv.services.MovieApiService
import com.example.mymv.services.TopRatedMovie
import kotlinx.android.synthetic.main.fragment_home_fragments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragments : Fragment() {

    private lateinit var homePosterAdapter: HomePosterAdapter
    private lateinit var listPosterAdapter: ListPosterAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
    : View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home_fragments, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePosterAdapter = HomePosterAdapter(mutableListOf(), object: HomePosterAdapter.OnAdapterListener{
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

        listPosterAdapter = ListPosterAdapter(mutableListOf(), object: ListPosterAdapter.OnAdapterListener{
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

        popular_movie_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popular_movie_list.setHasFixedSize(true)
        getMovieData { movies : List<Movie> ->
            popular_movie_list.adapter = HomePosterAdapter(movies,homePosterAdapter.listener)
        }

        top_rated_movie_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        top_rated_movie_list.setHasFixedSize(true)
        getTopRatedMovieData { movies : List<Movie> ->
            top_rated_movie_list.adapter = ListPosterAdapter(movies, listPosterAdapter.listener)
        }

        on_air_movie_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        on_air_movie_list.setHasFixedSize(true)
        getOnAirMovieData { movies : List<Movie> ->
            on_air_movie_list.adapter = ListPosterAdapter(movies, listPosterAdapter.listener)
        }
    }


    private fun getMovieData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }

    private fun getTopRatedMovieData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(TopRatedMovie::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }

    private fun getOnAirMovieData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(LatestMovieInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }






}