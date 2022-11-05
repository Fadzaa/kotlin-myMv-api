package com.example.mymv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mymv.databinding.ActivityLoginFormBinding

class LoginForm : AppCompatActivity() {
    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var validateButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_form)
        emailText = findViewById(R.id.editTextEmail)
        passwordText = findViewById(R.id.editTextPassword)
        validateButton = findViewById(R.id.loginButton)

        validateButton.setOnClickListener{
            val username = emailText.text.toString().trim()
            val password = passwordText.text.toString().trim()

            if(username.isEmpty()) {
                emailText.error = "Required Email"
                return@setOnClickListener

            }else if (password.isEmpty()) {
                passwordText.error = "Required Password"
                return@setOnClickListener
            }else if(username.isNotEmpty() && password.isNotEmpty()){
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, NavBar::class.java)
                startActivity(intent)
            }
        }


    }
}