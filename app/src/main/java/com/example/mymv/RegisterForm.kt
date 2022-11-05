package com.example.mymv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterForm : AppCompatActivity() {
    private lateinit var registerButton : Button
    private lateinit var fNameText : EditText
    private lateinit var lNameText : EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var cfmPasswordText : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_form)
        val signButton : Button = findViewById(R. id.signButton)

        fNameText = findViewById(R.id.editTextFirstName)
        lNameText = findViewById(R.id.editTextLastName)
        emailText = findViewById(R.id.editTextEmail)
        passwordText = findViewById(R.id.editTextPassword)
        cfmPasswordText= findViewById(R.id.editTextCfnPassword)
        registerButton= findViewById(R.id.registerButton)


        registerButton.setOnClickListener{
            val firstName = fNameText.text.toString().trim()
            val lastName = lNameText.text.toString().trim()
            val email = emailText.text.toString().trim()
            val password = passwordText.text.toString().trim()
            val confirmPassword = cfmPasswordText.text.toString().trim()
            if (firstName.isEmpty()) {
                fNameText.error = "Please enter your first name"
                return@setOnClickListener
            }else if (lastName.isEmpty()) {
                lNameText.error = "Please enter your last name"
                return@setOnClickListener
            }else if (email.isEmpty()) {
                emailText.error = "Please enter your email"
                return@setOnClickListener
            }else if (password.isEmpty()) {
                passwordText.error = "Please enter your password"
                return@setOnClickListener
            }else if (confirmPassword.isEmpty()) {
                cfmPasswordText.error = "Please confirm your password"
                return@setOnClickListener
            }else if( firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() ){
                Toast.makeText(this, "Register Complete", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginForm::class.java)
                startActivity(intent)

            }

        }

        signButton.setOnClickListener{
            val intent = Intent(this, LoginForm::class.java)
            startActivity(intent)
        }
    }
}