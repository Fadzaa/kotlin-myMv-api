package com.example.mymv.services.movieInterface

import com.example.mymv.models.MovieResponse
import com.example.mymv.models.TVResponse
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

interface ListGenreSeriesInterface {
    @GET("/3/tv/on_the_air")
    fun getListGenreSeries(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String
    ): Call<TVResponse>
}
