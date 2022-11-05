package com.example.mymv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.mymv.fragments.FilmFragment
import com.example.mymv.fragments.HomeFragments
import com.example.mymv.fragments.ProfileFragment
import com.example.mymv.fragments.WatchlistFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavBar : AppCompatActivity() {
    private val homeFragment = HomeFragments()
    private val filmFragment = FilmFragment()
    private val watchlistFragment = WatchlistFragment()
    private val profileFragment = ProfileFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navbar)
        replaceFragment(homeFragment)

        val bottom_navigation: BottomNavigationView = findViewById(R.id.navBar)

        bottom_navigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_film -> replaceFragment(filmFragment)
                R.id.ic_watchlist -> replaceFragment(watchlistFragment)
                R.id.ic_profile -> replaceFragment(profileFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        if(fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}