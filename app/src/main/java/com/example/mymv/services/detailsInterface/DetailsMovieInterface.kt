package com.example.mymv.services

import com.example.mymv.models.CastResponseModel
import com.example.mymv.models.MovieModel
import com.example.mymv.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailsMovieInterface {
    @GET("movie/{movieId}?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getMovieDetails(@Path("movieId") movieId: Int): Call<MovieModel>
}

interface CreditsMovieInterface {
    @GET("movie/{movieId}/credits?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getCreditDetails(@Path("movieId") movieId: Int): Call<CastResponseModel>
}

interface RecommendationMovieInterface {
    @GET("movie/{movieId}/recommendations?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getRecommendationDetails(@Path("movieId") movieId: Int): Call<MovieResponse>
}

