package com.prikshitkumar.foodies.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.prikshitkumar.foodies.R

class Profile : Fragment() {
    lateinit var userName: TextView
    lateinit var userEmail: TextView
    lateinit var userPhoneNo: TextView
    lateinit var userAddress: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        initializeTheFields(view)

        val sharedPreferences: SharedPreferences? = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        userName.text = sharedPreferences?.getString("userName", " ")
        userPhoneNo.text = sharedPreferences?.getString("mobileNo", " ")
        userEmail.text = sharedPreferences?.getString("email", " ")
        userAddress.text = sharedPreferences?.getString("address", " ")

        return view
    }

    fun initializeTheFields(view: View) {
        userName = view.findViewById(R.id.id_userPName)
        userEmail = view.findViewById(R.id.id_userPEmail)
        userPhoneNo = view.findViewById(R.id.id_userPPhoneNo)
        userAddress = view.findViewById(R.id.id_userPAddress)
    }
}