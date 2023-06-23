package com.example.mymv.initialPage

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.mymv.R
import com.example.mymv.databinding.ActivityLoginFormBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginForm : AppCompatActivity() {

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var validateButton: Button
    private lateinit var signUpButton: Button
    private lateinit var googleButton: Button
    private lateinit var guestButton: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var binding: ActivityLoginFormBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPrefs: SharedPreferences

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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
        progressBar = findViewById(R.id.pbLogin)

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
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"))) {
                Toast.makeText(this, "Please enter a valid email address (e.g., example@abc.co).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!password.matches(Regex("[a-zA-Z0-9]{5,}"))) {
                Toast.makeText(this, "Please enter a password with at least 5 characters, containing only letters and numbers.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            coroutineScope.launch {
                progressBar.visibility = View.VISIBLE

                try {
                    firebaseAuth.signInWithEmailAndPassword(email, password).await()
                    Toast.makeText(this@LoginForm, "Login Success", Toast.LENGTH_SHORT).show()
                    saveUserData(email, password)
                    val intent = Intent(this@LoginForm, NavBar::class.java)
                    startActivity(intent)
                } catch (exception: Exception) {
                    if (exception is FirebaseAuthInvalidUserException) {
                        Toast.makeText(this@LoginForm, "Invalid email or password.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginForm, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                } finally {
                    progressBar.visibility = View.GONE
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
        progressBar.visibility = View.VISIBLE

        firebaseAuth.signInAnonymously()
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE

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

        coroutineScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                val account = task.await()
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: Exception) {
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this@LoginForm, "Google sign in failed", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String?) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)

        try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()

            // Sign in success, update UI with the signed-in user's information
            val user = authResult.user
            Toast.makeText(this@LoginForm, "Signed in with Google", Toast.LENGTH_SHORT).show()

            // Save user data
            saveUserData(user?.displayName ?: "", user?.email ?: "")

            val intent = Intent(this@LoginForm, NavBar::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            // If sign in fails, display a message to the user.
            Toast.makeText(this@LoginForm, "Failed to sign in with Google", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        internal const val KEY_USERNAME = "username"
        internal const val KEY_EMAIL = "email"
    }
}
