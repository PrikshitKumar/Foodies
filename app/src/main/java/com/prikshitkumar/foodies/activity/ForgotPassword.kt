package com.prikshitkumar.foodies.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.prikshitkumar.foodies.R
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {

    lateinit var userPhoneNo: EditText
    lateinit var userEmailId: EditText
    lateinit var nextButton: Button
    lateinit var userPN: String
    lateinit var userMail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initializeTheFields()

        nextButton.setOnClickListener {
            userPN = userPhoneNo.text.toString()
            userMail = userEmailId.text.toString()

            when {
                TextUtils.isEmpty(userPhoneNo.text) -> {
                    /*If we want to perform any action on TEXTS then, TextUtil library has almost all the functions.
                      We doesn't need to made any function to perform any action...*/
                    userPhoneNo.setError("Required!")
                    /* setError() method shows the error message with RED MARK on that field, if required FIELD is EMPTY */
                }
                TextUtils.isEmpty(userEmailId.text) -> {
                    userEmailId.setError("Required!")
                }
                else -> {
                    val url = "http://13.235.250.119/v2/forgot_password/fetch_result/"
                    val queue = Volley.newRequestQueue(this@ForgotPassword)

                    val params = JSONObject()
                    params.put("mobile_number", userPN)
                    params.put("email", userMail)

                    val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, params,
                    Response.Listener {
                        try {
                            val json = it.getJSONObject("data")
                            val success = json.getBoolean("success")
                            if(success) {
                                val intent = Intent(this@ForgotPassword, ResetPassword::class.java)
                                intent.putExtra("mobile_number", userPN)
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(this@ForgotPassword, "Wrong Credentials!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e: Exception) {
                            Toast.makeText(this@ForgotPassword, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(applicationContext, "Check your Internet Connection!", Toast.LENGTH_SHORT).show()
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
    }

    fun initializeTheFields() {
        userPhoneNo = findViewById(R.id.id_userFPhoneNo)
        userEmailId = findViewById(R.id.id_userEmailId)
        nextButton = findViewById(R.id.id_nextButton)
    }
}