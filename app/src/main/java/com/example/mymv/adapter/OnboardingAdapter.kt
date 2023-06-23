package com.example.mymv.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.mymv.R
import com.example.mymv.models.OnboardingItem

class OnboardingAdapter(private val onboardingItems: List<OnboardingItem>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.onboarding_image)
        private val titleTextView: TextView = itemView.findViewById(R.id.text_point)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.text_description)

        fun bind(item: OnboardingItem) {
            imageView.setImageResource(item.image)
            titleTextView.text = item.title
            descriptionTextView.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = onboardingItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }
}