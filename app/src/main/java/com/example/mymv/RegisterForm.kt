package com.example.mymv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymv.databinding.ActivityRegisterFormBinding
import com.example.mymv.fragments.ProfileFragment
import com.example.mymv.fragments.WatchlistFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register_form.*
import kotlinx.android.synthetic.main.fragment_profile.*

class RegisterForm : AppCompatActivity() {
    private lateinit var registerButton: Button
    private lateinit var fNameText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText

    private lateinit var binding: ActivityRegisterFormBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        fNameText = binding.editTextFullName
        emailText = binding.editTextEmail
        passwordText = binding.editTextPassword
        registerButton = binding.signUpButton

        registerButton.setOnClickListener {
            val fullName = fNameText.text.toString()
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            if (fullName.isEmpty()) {
                fNameText.error = "Please enter your full name"
                return@setOnClickListener
            } else if (email.isEmpty()) {
                emailText.error = "Please enter your email"
                return@setOnClickListener
            } else if (password.isEmpty()) {
                passwordText.error = "Please enter your password"
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Register Complete", Toast.LENGTH_SHORT).show()

                    val intentText = Intent(this, NavBar::class.java)
                    intentText.putExtra("full_name", fullName)
                    intentText.putExtra("email", email)
                    startActivity(intentText)

                    val intent = Intent(this, LoginForm::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        val signInButton: Button = findViewById(R.id.signInButton)
        signInButton.setOnClickListener {
            val intentBtn = Intent(this, LoginForm::class.java)
            startActivity(intentBtn)
            finish()
        }
    }
}
