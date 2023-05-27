package com.example.mymv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.mymv.adapter.OnboardingAdapter
import com.example.mymv.models.OnboardingItem

class OnboardingScreen : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var dot1: ImageView
    private lateinit var dot2: ImageView
    private lateinit var dot3: ImageView
    private lateinit var nextButton: Button
    private lateinit var skipButton: Button

    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen)

        viewPager = findViewById(R.id.viewPager)
        dot1 = findViewById(R.id.dot1)
        dot2 = findViewById(R.id.dot2)
        dot3 = findViewById(R.id.dot3)
        nextButton = findViewById(R.id.nextButton)
        skipButton = findViewById(R.id.skipButton)


        skipButton.setOnClickListener{
            startActivity(Intent(this, LoginForm::class.java))
            finish()
        }



        dot1.setImageResource(R.drawable.active_indicator)
        dot2.setImageResource(R.drawable.inactive_indicator)
        dot3.setImageResource(R.drawable.inactive_indicator)

        val onboardingItems = listOf(
            OnboardingItem(getString(R.string.first_slide_point_text), getString(R.string.first_slide_desc), R.drawable.slide_image_1),
            OnboardingItem(getString(R.string.second_slide_point_text), getString(R.string.second_slide_desc), R.drawable.slide_image_2),
            OnboardingItem(getString(R.string.third_slide_point_text), getString(R.string.third_slide_desc), R.drawable.slide_image_3),
        )

        onboardingAdapter = OnboardingAdapter(onboardingItems)
        viewPager.adapter = onboardingAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDotIndicators(position)
                currentPosition = position

                if (position == 2) {
                    nextButton.text = getString(R.string.get_started_btn)

                } else {
                    nextButton.text = getString(R.string.next_btn)
                }
            }
        })

        nextButton.setOnClickListener {
            if (currentPosition < onboardingAdapter.itemCount - 1) {
                currentPosition++
                viewPager.currentItem = currentPosition

            } else {
                nextButton.setOnClickListener{
                    startActivity(Intent(this@OnboardingScreen, LoginForm::class.java))
                }
            }
        }
    }

    private fun updateDotIndicators(position: Int) {
        dot1.setImageResource(if (position == 0) R.drawable.active_indicator else R.drawable.inactive_indicator)
        dot2.setImageResource(if (position == 1) R.drawable.active_indicator else R.drawable.inactive_indicator)
        dot3.setImageResource(if (position == 2) R.drawable.active_indicator else R.drawable.inactive_indicator)
    }
}



