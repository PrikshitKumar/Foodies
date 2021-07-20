package com.prikshitkumar.foodies.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.prikshitkumar.foodies.R

class ConfirmationPage : AppCompatActivity() {
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation_page)

        button = findViewById(R.id.id_buttonOK)
        button.setOnClickListener {
            finish()
        }

    }

    override fun onBackPressed() {
        //nothing to do when back button pressed
    }
}