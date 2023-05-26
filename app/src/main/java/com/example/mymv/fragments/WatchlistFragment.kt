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
import com.example.mymv.models.MovieModel
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.MovieApiInterface
import com.example.mymv.services.RetrofitInstance
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
            override fun onClick(movieModel: MovieModel) {
                startActivity(Intent(requireContext() , MovieDetail::class.java)
                    .putExtra("id", movieModel.id)

                )
            }

        })
        upcoming_movie_list.layoutManager = LinearLayoutManager(requireContext())
        upcoming_movie_list.setHasFixedSize(true)
        getMovieData { movieModels : List<MovieModel> ->
            upcoming_movie_list.adapter = WatchlistAdapter(movieModels, watchlistAdapter.listener)
        }





    }



    private fun getMovieData(callback: (List<MovieModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movieModels)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }



    }