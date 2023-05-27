package com.example.mymv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mymv.fragments.HomeFragments
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if (firebaseAuth.currentUser != null) {
            Log.d("CheckUser", "User ${firebaseAuth.currentUser}")
            val intent = Intent(this, NavBar::class.java)
            startActivity(intent)
            finish()
        } else {
            val onboardingShown = sharedPrefs.getBoolean(KEY_ONBOARDING_SHOWN, false)
            if (onboardingShown) {
                navigateToHomeScreen()
            } else {
                GlobalScope.launch {
                    delay(2000)
                    navigateToOnboardingScreen()
                }
            }
        }
    }

    private fun navigateToOnboardingScreen() {
        val intent = Intent(this@SplashScreen, OnboardingScreen::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this@SplashScreen, NavBar::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val KEY_ONBOARDING_SHOWN = "onboarding_shown"
    }
}
