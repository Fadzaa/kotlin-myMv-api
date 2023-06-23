package com.example.mymv.initialPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mymv.BaseActivity
import com.example.mymv.R
import com.example.mymv.fragments.SeriesFragment
import com.example.mymv.fragments.HomeFragments
import com.example.mymv.fragments.ProfileFragment
import com.example.mymv.fragments.WatchlistFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class  NavBar : BaseActivity() {
    private val homeFragment = HomeFragments()
    private val seriesFragment = SeriesFragment()
    private val watchlistFragment = WatchlistFragment()
    private val profileFragment = ProfileFragment()
    private val firestoreDB = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navbar)
        replaceFragment(homeFragment)




        val bottom_navigation: BottomNavigationView = findViewById(R.id.navBar)



        bottom_navigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_film -> replaceFragment(seriesFragment)
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