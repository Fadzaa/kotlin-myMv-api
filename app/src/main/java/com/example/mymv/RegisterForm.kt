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
    private lateinit var registerButton : Button
    private lateinit var fNameText : EditText
    private lateinit var lNameText : EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var cfmPasswordText : EditText

    private lateinit var binding: ActivityRegisterFormBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_form)
        binding = ActivityRegisterFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val signButton : Button = findViewById(R. id.signButton)

        fNameText = findViewById(R.id.editTextFirstName)
        lNameText = findViewById(R.id.editTextLastName)
        emailText = findViewById(R.id.editTextEmail)
        passwordText = findViewById(R.id.editTextPassword)
        cfmPasswordText = findViewById(R.id.editTextCfnPassword)
        registerButton = findViewById(R.id.registerButton)


        binding.registerButton.setOnClickListener{
            val firstName = fNameText.text.toString()
            val lastName = lNameText.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextCfnPassword.text.toString()


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
            }else if (confirmPassword != password) {
                cfmPasswordText.error = "Password doesn't match"
                return@setOnClickListener
            }else if( firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() ){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(this, "Register Complete", Toast.LENGTH_SHORT).show()

                            val intentText = Intent(this, NavBar::class.java)
                            intentText.putExtra("first_name",fNameText.text.toString())
                            intentText.putExtra("last_name",lNameText.text.toString())
                            intentText.putExtra("email",emailText.text.toString())
                            startActivity(intentText)

                            val intent = Intent(this, LoginForm::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else(
                        Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
                )



            }




        }




        signButton.setOnClickListener{
            val intentBtn = Intent(this, LoginForm::class.java)
            startActivity(intentBtn)


        }
    }
}