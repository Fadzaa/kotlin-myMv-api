package com.example.mymv.services

import com.example.mymv.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ListGenreMovieInterface {
    @GET("/3/movie/now_playing")
    fun getListGenreMovie(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String
    ): Call<MovieResponse>
}
