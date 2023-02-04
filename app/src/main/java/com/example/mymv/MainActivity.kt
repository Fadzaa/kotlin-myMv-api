package com.example.mymv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymv.databinding.ActivityLoginFormBinding
import com.example.mymv.models.Movie
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.MovieApiInterface
import com.example.mymv.services.MovieApiService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home_fragments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginFormBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, NavBar::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginFormBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        val splashButton : Button = findViewById(R.id.splashButton)

        splashButton.setOnClickListener {
            val intent = Intent(this, RegisterForm::class.java)
            startActivity(intent)
        }


    }






}