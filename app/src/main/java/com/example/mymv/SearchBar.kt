package com.example.mymv

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymv.adapter.WatchlistAdapter
import com.example.mymv.models.MovieModel
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.SearchApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchBar : AppCompatActivity() {

    private lateinit var searchApiInterface: SearchApiInterface
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieAdapter: WatchlistAdapter
    private lateinit var arrowButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_bar)

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
        searchApiInterface = retrofit.create(SearchApiInterface::class.java)
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
        movieRecyclerView = findViewById(R.id.rvSearchBar)
        movieRecyclerView.layoutManager = LinearLayoutManager(this)

// Initialize WatchlistAdapter
        movieAdapter = WatchlistAdapter(mutableListOf(), object : WatchlistAdapter.OnAdapterListener {
            override fun onClick(movieModel: MovieModel) {
                // Handle item click event
            }
        })

// Set the adapter to RecyclerView
        movieRecyclerView.adapter = movieAdapter

    }

    private fun performSearch(query: String) {
        // Make API call to search movies
        val apiKey = "api_key=2d51650e8cf7b5a2d13b814001a0dd30" // Replace with your actual API key
        val call = searchApiInterface.searchMovies(apiKey, query)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    val movieModels = movieResponse?.movieModels
                    Log.d("API_RESPONSE", "Movie Models: $movieModels")
                    // Update the movie list in the adapter
                    movieAdapter.updateData(movieModels)
                } else {
                    Log.e("API_RESPONSE", "API call failed with response code: ${response.code()}")
                    // Handle API call failure
                    // Handle API call failure
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                // Handle API call failure
            }
        })
    }

}
