package com.example.mymv

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.mymv.NavBar
import com.example.mymv.R
import com.example.mymv.RegisterForm
import com.example.mymv.databinding.ActivityLoginFormBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider

class LoginForm : AppCompatActivity() {

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var validateButton: Button
    private lateinit var signUpButton: Button
    private lateinit var googleButton: Button
    private lateinit var guestButton: Button

    private lateinit var binding: ActivityLoginFormBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPrefs: SharedPreferences

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            handleGoogleSignInResult(data)
        } else {
            Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        emailText = findViewById(R.id.editTextEmail)
        passwordText = findViewById(R.id.editTextPassword)
        validateButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUpButton)

        val button = findViewById<Button>(R.id.btnSignInWithGoogle)
        val backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.google_button)
        button.background = backgroundDrawable

        signUpButton.setOnClickListener {
            val intent = Intent(this, RegisterForm::class.java)
            startActivity(intent)
            finish()
        }

        guestButton = findViewById(R.id.guestButton)
        guestButton.setOnClickListener {
            signInAsGuest()
        }

        validateButton.setOnClickListener {
            val email = emailText.text.toString().trim()
            val password = passwordText.text.toString().trim()

            if (email.isEmpty()) {
                emailText.error = "Required Email"
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"))) {
                emailText.error = "Invalid Email"
                Toast.makeText(this, "Please enter a valid email address (e.g., example@abc.co).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                passwordText.error = "Required Password"
                Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!password.matches(Regex("[a-zA-Z0-9]{5,}"))) {
                passwordText.error = "Invalid Password"
                Toast.makeText(this, "Please enter a password with at least 5 characters, containing only letters and numbers.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()

                        // Save user data
                        saveUserData(email, password)

                        val intent = Intent(this, NavBar::class.java)
                        startActivity(intent)
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthInvalidUserException) {
                            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        googleButton = findViewById(R.id.btnSignInWithGoogle)
        googleButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun saveUserData(username: String, email: String) {
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_EMAIL, email)
        editor.apply()
    }

    private fun getUserData(): Pair<String?, String?> {
        val username: String? = sharedPrefs.getString(KEY_USERNAME, null)
        val email: String? = sharedPrefs.getString(KEY_EMAIL, null)
        return Pair(username, email)
    }

    private fun signInAsGuest() {
        firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Signed in as Guest", Toast.LENGTH_SHORT).show()

                // Save user data
                saveUserData("Guest", "")

                val intent = Intent(this, NavBar::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to sign in as Guest", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken)
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
            Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth.currentUser
                    Toast.makeText(this, "Signed in with Google", Toast.LENGTH_SHORT).show()

                    // Save user data
                    saveUserData(user?.displayName ?: "", user?.email ?: "")

                    val intent = Intent(this, NavBar::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Failed to sign in with Google", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
    }
}
