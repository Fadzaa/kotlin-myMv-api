package com.example.mymv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymv.adapter.details.PosterCreditsAdapter
import com.example.mymv.adapter.details.RecommendationAdapter
import com.example.mymv.models.ActorModel
import com.example.mymv.models.CastResponseModel
import com.example.mymv.models.MovieModel
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.ActorMovieCreditsInterface
import com.example.mymv.services.CreditsMovieInterface
import com.example.mymv.services.DetailsActorInterface
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ActorDetail : AppCompatActivity() {

    private val IMAGE_BASE = "https://image.tmdb.org/t/p/original/"
    private val BASE_URL = "https://api.themoviedb.org/3/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor_detail)

        //PASSED ID
        val actorID = intent.getStringExtra("actorId")

        val actorIDNum = actorID!!.toInt()



        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: DetailsActorInterface = retrofit.create(DetailsActorInterface::class.java)

        val call: Call<ActorModel> = apiService.getActorDetails(actorIDNum)
        println(call)

        call.enqueue(object: Callback<ActorModel> {
            override fun onResponse(call: Call<ActorModel>, response: Response<ActorModel>) {
                if (response.isSuccessful){
                    val actorModel : ActorModel? = response.body()
                    actorModel?.let {
                        val actorName: TextView = findViewById(R.id.tvActorName)
                        val actorImage: ImageView = findViewById(R.id.ivActor)
                        val actorBiography: TextView = findViewById(R.id.tvActorBiography)
                        val actorBirthday: TextView = findViewById(R.id.tvActorBirthday)
                        val actorGender: TextView = findViewById(R.id.tvActorGender)
                        val actorPlaceBirth: TextView = findViewById(R.id.tvActorPlace)


                        val getName = actorModel.actorName
                        val getImage = actorModel.actorImage
                        val getBiography = actorModel.actorBiography
                        val getBirthday = actorModel.actorBirthday
                        val getGender = actorModel.actorGender
                        val getPlaceBirth = actorModel.actorPlaceBirth

                        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                        try {
                            val date = inputFormat.parse(getBirthday)
                            val outputDate = outputFormat.format(date)
                            println(outputDate)

                            actorBirthday.text = outputDate
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }

                        val genderText = when (getGender) {
                            "1" -> "(Female)"
                            "2" -> "(Male)"
                            else -> ""
                        }

                        actorName.text = getName
                        actorBiography.text = getBiography
                        actorGender.text = genderText
                        actorPlaceBirth.text = getPlaceBirth

                        Glide.with(this@ActorDetail)
                            .load(IMAGE_BASE + getImage)
                            .into(actorImage)
                    }
                }else {
                    // Handle error response
                    val errorCode = response.code()
                    val errorMessage = response.message()
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<ActorModel>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        val apiMovieCreditService:ActorMovieCreditsInterface= retrofit.create(
            ActorMovieCreditsInterface::class.java)

        val callCredit: Call<CastResponseModel> = apiMovieCreditService.getActorMovieCredits(actorIDNum)

        callCredit.enqueue(object: Callback<CastResponseModel> {
            override fun onResponse(call: Call<CastResponseModel>, response: Response<CastResponseModel>) {
                if (response.isSuccessful) {
                    val movieResponse: CastResponseModel? = response.body()
                    val movieResults: List<ActorModel>? = movieResponse?.castList
                    movieResults?.let {
                        val rvKnown = findViewById<RecyclerView>(R.id.rvKnownFor)
                        val creditAdapter = PosterCreditsAdapter(this@ActorDetail, movieResults)
                        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                            applicationContext, LinearLayoutManager.HORIZONTAL, false
                        )
                        rvKnown.layoutManager = mLayoutManager
                        rvKnown.adapter = creditAdapter
                    }
                } else {
                    // Handle error response
                    val errorCode = response.code()
                    val errorMessage = response.message()
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<CastResponseModel>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }
}