package com.example.mymv.fragments

import android.content.Intent
import android.os.Bundle

import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymv.detail.MovieDetail
import com.example.mymv.R
import com.example.mymv.adapter.WatchlistAdapter
import com.example.mymv.models.MovieModel
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.movieInterface.MoviePopularInterface
import com.example.mymv.services.RetrofitInstance
import com.example.mymv.services.movieInterface.UpcomingMoviesInterface
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

        val mutableList = emptyList<MovieModel>().toMutableList()

        watchlistAdapter = WatchlistAdapter(mutableList, object : WatchlistAdapter.OnAdapterListener {
            override fun onClick(movieModel: String) {
                startActivity(
                    Intent(requireContext(), MovieDetail::class.java)
                        .putExtra("id", movieModel)
                )
            }

        })

        upcoming_movie_list.layoutManager = LinearLayoutManager(requireContext())
        upcoming_movie_list.setHasFixedSize(true)
        upcoming_movie_list.adapter = watchlistAdapter



//        getMovieData { movieModels: List<MovieModel> ->
//            watchlistAdapter.updateData(movieModels)
//        }

        upcoming_movie_list.layoutManager = LinearLayoutManager(requireContext())
        upcoming_movie_list.setHasFixedSize(true)
        getMovieData { movieModelsMutable : MutableList<MovieModel> ->
            upcoming_movie_list.adapter = WatchlistAdapter(movieModelsMutable, watchlistAdapter.listener)
        }




    }



    private fun getMovieData(callback: (MutableList<MovieModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(UpcomingMoviesInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movieModels)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }




    }