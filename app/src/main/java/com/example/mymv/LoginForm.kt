package com.example.mymv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mymv.databinding.ActivityLoginFormBinding
import com.google.firebase.auth.FirebaseAuth

class LoginForm : AppCompatActivity() {
    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var validateButton : Button
    private lateinit var signUpButton: Button

    private lateinit var binding : ActivityLoginFormBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        emailText = findViewById(R.id.editTextEmail)
        passwordText = findViewById(R.id.editTextPassword)
        validateButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener{
            val intent = Intent(this, RegisterForm::class.java)
            startActivity(intent)
        }

        validateButton.setOnClickListener{
            val email = emailText.text.toString().trim()
            val password = passwordText.text.toString().trim()

            if(email.isEmpty()) {
                emailText.error = "Required Email"
                return@setOnClickListener

            }else if (password.isEmpty()) {
                passwordText.error = "Required Password"
                return@setOnClickListener
            }else if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, NavBar::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Username or Password is wrong", Toast.LENGTH_SHORT).show()
                    }


                }

            }
        }

    }



}