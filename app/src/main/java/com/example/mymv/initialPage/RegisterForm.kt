package com.example.mymv.initialPage


import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mymv.R
import com.example.mymv.databinding.ActivityRegisterFormBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterForm : AppCompatActivity() {
    private lateinit var registerButton: Button
    private lateinit var fNameText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var googleButton: Button
    private val firestoreDB = FirebaseFirestore.getInstance()


    private lateinit var binding: ActivityRegisterFormBinding
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
        binding = ActivityRegisterFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.pbRegister)

        fNameText = binding.editTextFullName
        emailText = binding.editTextEmail
        passwordText = binding.editTextPassword
        registerButton = binding.signUpButton

        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        googleButton = findViewById(R.id.btnSignUpWithGoogle)
        googleButton.setOnClickListener {
            signInWithGoogle()
        }

        registerButton.setOnClickListener {
            val fullName = fNameText.text.toString()
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            if (fullName.isEmpty()) {
                Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }  else if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"))) {
                Toast.makeText(this, "Please enter a valid email address (e.g., example@abc.co).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!password.matches(Regex("[a-zA-Z0-9]{5,}"))) {
                Toast.makeText(
                    this,
                    "Please enter a password with at least 5 characters, containing only letters and numbers.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            coroutineScope.launch {
                progressBar.visibility = ProgressBar.VISIBLE

                try {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()

                    val currentUserID = firebaseAuth.currentUser?.uid
                    val user = hashMapOf(
                        "fullName" to fullName,
                        "email" to email
                    )

                    currentUserID?.let {
                        firestoreDB.collection("users")
                            .document(currentUserID)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this@RegisterForm, "Register Complete", Toast.LENGTH_SHORT).show()

                                // Start home activity with full name and email as extras
                                val intentText = Intent(this@RegisterForm, NavBar::class.java)
                                intentText.putExtra("full_name", fullName)
                                intentText.putExtra("email", email)
                                startActivity(intentText)

                                val intent = Intent(this@RegisterForm, LoginForm::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this@RegisterForm, "Register failed. Please try again.", Toast.LENGTH_SHORT).show()
                                exception.printStackTrace()
                            }
                    }

                    Toast.makeText(this@RegisterForm, "Register Complete", Toast.LENGTH_SHORT).show()

                    val intentText = Intent(this@RegisterForm, NavBar::class.java)
                    intentText.putExtra("full_name", fullName)
                    intentText.putExtra("email", email)
                    startActivity(intentText)

                    val intent = Intent(this@RegisterForm, LoginForm::class.java)
                    startActivity(intent)
                } catch (exception: Exception) {
                    Toast.makeText(this@RegisterForm, "Register failed. Please try again.", Toast.LENGTH_SHORT).show()
                    exception.printStackTrace()
                } finally {
                    progressBar.visibility = ProgressBar.GONE
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

    private fun saveUserData(username: String, email: String) {
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString(LoginForm.KEY_USERNAME, username)
        editor.putString(LoginForm.KEY_EMAIL, email)
        editor.apply()
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
                Log.w(ContentValues.TAG, "Google sign in failed", e)
                Toast.makeText(this@RegisterForm, "Google sign in failed", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this@RegisterForm, "Signed in with Google", Toast.LENGTH_SHORT).show()

            // Save user data
            saveUserData(user?.displayName ?: "", user?.email ?: "")

            val intent = Intent(this@RegisterForm, NavBar::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            // If sign in fails, display a message to the user.
            Toast.makeText(this@RegisterForm, "Failed to sign in with Google", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
    }
}
