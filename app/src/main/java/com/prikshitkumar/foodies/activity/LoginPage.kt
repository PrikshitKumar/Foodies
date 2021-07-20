 package com.prikshitkumar.foodies.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.prikshitkumar.foodies.MainActivity
import com.prikshitkumar.foodies.R
import org.json.JSONObject

class LoginPage : AppCompatActivity() {

    lateinit var userPhoneNo: EditText
    lateinit var userPassword: EditText
    lateinit var login: Button
    lateinit var forgotPassword: TextView
    lateinit var signUp: TextView

    lateinit var userPN: String
    lateinit var userPwd: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        initializeTheFields()
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        login.setOnClickListener {
            userPN= userPhoneNo.text.toString()
            userPwd = userPassword.text.toString()

            when {
                TextUtils.isEmpty(userPhoneNo.text) -> {
                    userPhoneNo.setError("Required!")
                }
                TextUtils.isEmpty(userPassword.text) -> {
                    userPassword.setError("Required!")
                }
                (userPhoneNo.text.length < 10) -> {
                    userPhoneNo.setError("Enter 10 digits")
                }
                else -> {
                    val jsonParams = JSONObject()  /* If we want to send the data to the Server, then it is also in the form of String/
                    JSON/etc. Here we are using JSONObject. jsonParams variable hold the values like credentials (Email, Password, etc)
                    for fetching the required data from Server. */
                    jsonParams.put("mobile_number", userPN)
                    jsonParams.put("password", userPwd)

                    val url = "http://13.235.250.119/v2/login/fetch_result/"
                    val queue = Volley.newRequestQueue(this@LoginPage)

                    val jsonRequest = object : JsonObjectRequest (Request.Method.POST, url, jsonParams,
                        Response.Listener {
                            try {
                                val json = it.getJSONObject("data")
                                val success = json.getBoolean("success")
                                if (success) {
                                    val data = json.getJSONObject("data")

                                    editor.putBoolean("first_Start", false)
                                    editor.putString("user_id", data.getString("user_id"))
                                    editor.putString("userName", data.getString("name"))
                                    editor.putString("mobileNo", data.getString("mobile_number"))
                                    editor.putString("email", data.getString("email"))
                                    editor.putString("address", data.getString("address"))
                                    editor.apply()  /* After adding or removing the values from SharedPreferences, it is mandatory to call
                                        the apply() method for commit the changes. */

                                    val intent = Intent(this@LoginPage, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else {
                                    Toast.makeText(this@LoginPage, "Wrong Credentials!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            catch(e: Exception) {
                                Toast.makeText(this@LoginPage, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            Toast.makeText(this@LoginPage, "Check your Internet Connection!", Toast.LENGTH_SHORT).show()
                            }
                    ) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-Type"] = "application/json"
                            headers["token"] = "9bf534118365f1"
                            return headers
                        }
                    }
                    queue.add(jsonRequest)
                }
            }
        }

        forgotPassword.setOnClickListener {
            val intent = Intent(this@LoginPage, ForgotPassword::class.java)
            startActivity(intent)
        }

        signUp.setOnClickListener {
            val intent = Intent(this@LoginPage, Registration::class.java)
            startActivity(intent)
        }
    }

    fun initializeTheFields() {
        userPhoneNo = findViewById(R.id.id_userPhoneNo)
        userPassword = findViewById(R.id.id_userPassword)
        login = findViewById(R.id.id_loginButton)
        forgotPassword = findViewById(R.id.id_forgotPassword)
        signUp = findViewById(R.id.id_signUp)
    }
}