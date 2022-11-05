package com.example.mymv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
                val splashButton : Button = findViewById(R.id.splashButton)

            splashButton.setOnClickListener {
                val intent = Intent(this, RegisterForm::class.java)
                startActivity(intent)
            }
    }
}