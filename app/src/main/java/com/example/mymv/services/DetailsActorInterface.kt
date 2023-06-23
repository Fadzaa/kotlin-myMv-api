package com.example.mymv.services

import com.example.mymv.models.ActorModel
import com.example.mymv.models.CastResponseModel
import com.example.mymv.models.MovieResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailsActorInterface {
    @GET("person/{personId}?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getActorDetails(@Path("personId") movieId: Int): Call<ActorModel>
}

interface ActorMovieCreditsInterface {
    @GET("person/{personId}/movie_credits?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getActorMovieCredits(@Path("personId") movieId: Int): Call<CastResponseModel>
}