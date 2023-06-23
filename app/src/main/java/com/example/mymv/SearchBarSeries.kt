package com.example.mymv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymv.adapter.seriesAdapter.WatchlistSeriesAdapter
import com.example.mymv.detail.SeriesDetail
import com.example.mymv.models.TVModel
import com.example.mymv.models.TVResponse
import com.example.mymv.services.SearchApiSeriesInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchBarSeries : AppCompatActivity() {

    private lateinit var searchApiInterface: SearchApiSeriesInterface
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var tvAdapter: WatchlistSeriesAdapter
    private lateinit var arrowButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_bar_series)

        arrowButton = findViewById(R.id.ivArrow)

        arrowButton.setOnClickListener {
            finish()
        }

        // Create Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create SearchApiInterface
        searchApiInterface = retrofit.create(SearchApiSeriesInterface::class.java)
//        performSearch(query = "Avengers")

        // Set EditorActionListener for the search EditText
        // Set EditorActionListener for the search EditText
        val etSearchText = findViewById<EditText>(R.id.etSearchText)

        etSearchText.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_DONE -> {
                    val query = etSearchText.text.toString()
                    performSearch(query)
                    true
                }

                EditorInfo.IME_ACTION_NEXT -> {
                    val query = etSearchText.text.toString()
                    performSearch(query)
                    true
                }
                else -> false
            }
        }



        // Initialize RecyclerView
        movieRecyclerView = findViewById(R.id.rvSearchBarSeries)
        movieRecyclerView.layoutManager = LinearLayoutManager(this)

// Initialize WatchlistAdapter
        tvAdapter = WatchlistSeriesAdapter(mutableListOf(), object : WatchlistSeriesAdapter.OnAdapterListener {
            override fun onClick(tvModel: TVModel) {
                // Handle item click event
                startActivity(Intent(this@SearchBarSeries, SeriesDetail::class.java)
                    .putExtra("tv_id", tvModel.id)
                )

            }
        })

// Set the adapter to RecyclerView
        movieRecyclerView.adapter = tvAdapter

    }

    private fun performSearch(query: String) {
        // Make API call to search movies
        val apiKey = "api_key=2d51650e8cf7b5a2d13b814001a0dd30" // Replace with your actual API key
        val call = searchApiInterface.searchSeries(apiKey, query)
        call.enqueue(object : Callback<TVResponse> {
            override fun onResponse(call: Call<TVResponse>, response: Response<TVResponse>) {
                if (response.isSuccessful) {
                    val tvResponse = response.body()
                    val tvModels = tvResponse?.tvModels
                    Log.d("API_RESPONSE", "Movie Models: $tvModels")
                    // Update the movie list in the adapter
                    tvAdapter.updateData(tvModels)
                } else {
                    Log.e("API_RESPONSE", "API call failed with response code: ${response.code()}")
                    // Handle API call failure
                    // Handle API call failure
                }
            }

            override fun onFailure(call: Call<TVResponse>, t: Throwable) {
                // Handle API call failure
            }
        })
    }

}