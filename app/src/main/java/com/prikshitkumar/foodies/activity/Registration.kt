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
import com.prikshitkumar.foodies.MainActivity
import com.prikshitkumar.foodies.R
import org.json.JSONObject

class Registration : AppCompatActivity() {

    lateinit var userName: EditText
    lateinit var userEmail: EditText
    lateinit var userPhoneNo: EditText
    lateinit var userAddress: EditText
    lateinit var userPassword: EditText
    lateinit var userConfirmPassword: EditText
    lateinit var register: Button
    lateinit var name: String
    lateinit var email: String
    lateinit var phoneNo: String
    lateinit var address: String
    lateinit var pwd: String
    lateinit var confirmPwd: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)

        initializeTheFields()
        register.setOnClickListener {
            name = userName.text.toString()
            email = userEmail.text.toString()
            phoneNo = userPhoneNo.text.toString()
            address = userAddress.text.toString()
            pwd = userPassword.text.toString()
            confirmPwd = userConfirmPassword.text.toString()


            when {
                TextUtils.isEmpty(userName.text) -> {
                    userName.setError("Required!")
                }
                TextUtils.isEmpty(userEmail.text) -> {
                    userEmail.setError("Required!")
                }
                TextUtils.isEmpty(userPhoneNo.text) -> {
                    userPhoneNo.setError("Required!")
                }
                TextUtils.isEmpty(userAddress.text) -> {
                    userAddress.setError("Required!")
                }
                TextUtils.isEmpty(userPassword.text) -> {
                    userPassword.setError("Required!")
                }
                TextUtils.isEmpty(userConfirmPassword.text) -> {
                    userConfirmPassword.setError("Required!")
                }
                (userName.text.length < 3) -> {
                    userName.setError("Min Length should be maintained!")
                }
                (userPhoneNo.text.length < 10) -> {
                    userPhoneNo.setError("Enter 10 digits")
                }
                (pwd.length < 4) -> {
                    userPassword.setError("Min Length should be maintained!")
                }
                !(pwd.equals(confirmPwd)) -> {
                    userConfirmPassword.setError("Password should be Same!")
                }
                else -> {
                    val url = "http://13.235.250.119/v2/register/fetch_result"
                    val queue = Volley.newRequestQueue(this@Registration)

                    val params = JSONObject()
                    params.put("name", name)
                    params.put("mobile_number", phoneNo)
                    params.put("password", confirmPwd)
                    params.put("address", address)
                    params.put("email", email)

                    val jsonRequest = object: JsonObjectRequest(Request.Method.POST, url, params,
                    Response.Listener {
                        try {
                            val json = it.getJSONObject("data")
                            val success = json.getBoolean("success")
                            if (success) {
                                Toast.makeText(this@Registration, "Successfully Registered", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@Registration, LoginPage::class.java)
                                startActivity(intent)
                                finishAffinity()
                            }
                            else {
                                Toast.makeText(this@Registration, "Already Registered!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e: Exception) {
                            Toast.makeText(this@Registration, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(this@Registration, "Check your Internet Connection!", Toast.LENGTH_SHORT).show()
                    }) {
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
        userName = findViewById(R.id.id_userRName)
        userEmail = findViewById(R.id.id_userREmail)
        userPhoneNo = findViewById(R.id.id_userRPhoneNo)
        userAddress = findViewById(R.id.id_userRAddress)
        userPassword = findViewById(R.id.id_userRePassword)
        userConfirmPassword = findViewById(R.id.id_userReConfirmPassword)
        register = findViewById(R.id.id_regiterationButton)
    }
}