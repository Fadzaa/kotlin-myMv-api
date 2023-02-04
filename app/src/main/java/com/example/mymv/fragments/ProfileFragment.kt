package com.example.mymv.fragments

import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.parseIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mymv.LoginForm
import com.example.mymv.R
import com.example.mymv.RegisterForm
import com.example.mymv.databinding.ActivityLoginFormBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_navbar.*


class ProfileFragment : Fragment() {


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginFormBinding
    private lateinit var fName: TextView
    private lateinit var lName: TextView
    private lateinit var email: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = ActivityLoginFormBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val logoutBtn: Button = view.findViewById(R.id.logoutButton)

        fName = view.findViewById(R.id.first_name) as TextView
//        lName = view.findViewById(R.id.last_name) as TextView
        email = view.findViewById(R.id.email) as TextView

        val getBundle = this.arguments
        val firstName = getBundle?.getString("first_name")
        val lastName = getBundle?.getString("last_name")
        val emailText = getBundle?.getString("email")

//        fName.text = firstName
//        lName.text = lastName
//        email.text = emailText



        logoutBtn.setOnClickListener {
            val intent = Intent(requireContext(), LoginForm::class.java)
            startActivity(intent)
            firebaseAuth.signOut()

        }

        return view
    }



}