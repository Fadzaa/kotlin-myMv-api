package com.example.mymv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymv.adapter.seriesAdapter.WatchlistSeriesAdapter
import com.example.mymv.detail.SeriesDetail
import com.example.mymv.models.TVModel
import com.example.mymv.models.TVResponse
import com.example.mymv.services.movieInterface.ListGenreSeriesInterface
import com.example.mymv.services.RetrofitInstance
import kotlinx.android.synthetic.main.activity_list_genre_series.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListGenreSeries : AppCompatActivity() {
    private lateinit var genreName: TextView
    private lateinit var genreSlogan: TextView
    private lateinit var listGenreSeriesAdapter: WatchlistSeriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_genre_series)

        genreName = findViewById(R.id.tvGenreName)
        genreSlogan= findViewById(R.id.tvSloganGenre)

        val genreListName = intent.getStringExtra("genre_name")

        genreName.text = genreListName
        genreSlogan.text = "List of $genreListName Series"


        val mutableList = emptyList<TVModel>().toMutableList()



        listGenreSeriesAdapter = WatchlistSeriesAdapter(mutableList, object: WatchlistSeriesAdapter.OnAdapterListener{
            override fun onClick(tvModel: TVModel) {

                Log.d("ErrorCheck", "This is ${tvModel.overview}")
                startActivity(
                    Intent(this@ListGenreSeries, SeriesDetail::class.java)
                    .putExtra("tv_id", tvModel.id)
                )


            }

        })


        rvListGenreSeries.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvListGenreSeries.setHasFixedSize(true)
        getListGenreMovie{ movieModelsMutable : MutableList<TVModel> ->
            rvListGenreSeries.adapter = WatchlistSeriesAdapter(movieModelsMutable, listGenreSeriesAdapter.listener)
        }
    }



    private fun getListGenreMovie(callback: (MutableList<TVModel>) -> Unit) {
        val apiKey = "2d51650e8cf7b5a2d13b814001a0dd30"
        val genreID = intent.getStringExtra("genre_id")
        val apiService = RetrofitInstance.getInstance().create(ListGenreSeriesInterface::class.java)

        apiService.getListGenreSeries(apiKey , genreID.toString()).enqueue(object : Callback<TVResponse> {

            override fun onResponse(call: Call<TVResponse>, response: Response<TVResponse>) {
                return callback(response.body()!!.tvModels)
            }

            override fun onFailure(call: Call<TVResponse>, t: Throwable) {

            }

        })
    }
}