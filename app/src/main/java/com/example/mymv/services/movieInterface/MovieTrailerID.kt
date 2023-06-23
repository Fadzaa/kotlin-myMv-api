package com.example.mymv.services.movieInterface

import android.os.Parcelable
import com.example.mymv.models.MovieResponse

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieTrailerID {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>


    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailers(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Response<TrailerResponse>
}

@Parcelize
data class TrailerModel(
    @SerializedName("key")
    val key: String?,

    @SerializedName("name")
val name: String?
) : Parcelable {
    constructor() : this("","")
}

@Parcelize
data class TrailerResponse(
    @SerializedName("results")
    val trailers: MutableList<TrailerModel>
) : Parcelable {
    constructor() : this(mutableListOf())
}

object MovieIDClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val API_KEY = "2d51650e8cf7b5a2d13b814001a0dd30"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val movieApiService: MovieTrailerID = retrofit.create(MovieTrailerID::class.java)

    suspend fun getNowPlayingMovieIds(): List<String> {
        val response = movieApiService.getNowPlayingMovies(API_KEY)
        if (response.isSuccessful) {
            val movieResponse = response.body()
            return movieResponse?.movieModels?.map { it.id.toString() } ?: emptyList()
        } else {
            throw ApiException(Status(response.code()))
        }
    }

    suspend fun getMovieTrailers(movieId: String): List<String> {
        val response = movieApiService.getMovieTrailers(movieId, API_KEY)
        if (response.isSuccessful) {
            val trailerResponse = response.body()
            val trailers = trailerResponse?.trailers ?: emptyList()
            return trailers.take(1).map { "https://www.youtube.com/watch?v=${it.key}" }
        } else if (response.code() == 404) {
            // Handle the case where trailers are not found for the movie ID
            // For example, you can return an empty list or throw a specific exception.
            return emptyList()
        } else {
            throw ApiException(Status(response.code()))
        }
    }

}


