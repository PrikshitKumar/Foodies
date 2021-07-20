package com.prikshitkumar.foodies.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.prikshitkumar.foodies.MainActivity
import com.prikshitkumar.foodies.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val boolean = sharedPreferences.getBoolean("first_Start", true)
        if(boolean) {
            Handler().postDelayed({
                val intent = Intent(this@SplashScreen, LoginPage::class.java)
                startActivity(intent)
            },2000)
        }
        else {
            Handler().postDelayed({
                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(intent)
            },2000)
        }
    }
}