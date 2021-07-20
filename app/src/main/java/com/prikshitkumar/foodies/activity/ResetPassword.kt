package com.prikshitkumar.foodies.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.prikshitkumar.foodies.R
import org.json.JSONObject

class ResetPassword : AppCompatActivity() {

    lateinit var otp: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var confirm: Button
    lateinit var pwd: String
    lateinit var confirmPwd: String
    lateinit var otpStr: String
    lateinit var userPhoneNo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        initializeTheFields()

        userPhoneNo = intent.getStringExtra("mobile_number").toString()

        confirm.setOnClickListener {
            otpStr = otp.text.toString()
            pwd = password.text.toString()
            confirmPwd = confirmPassword.text.toString()

            when {
                TextUtils.isEmpty(otp.text) -> {
                    otp.setError("Required!")
                }
                TextUtils.isEmpty(password.text) -> {
                    password.setError("Required!")
                }
                TextUtils.isEmpty(confirmPassword.text) -> {
                    confirmPassword.setError("Required!")
                }
                (pwd.length < 4) -> {
                    password.setError("Min length should be maintained!")
                }
                !(pwd.equals(confirmPwd)) -> {
                    confirmPassword.setError("Password should be Same!")
                }
                else -> {
                    val params = JSONObject()
                    params.put("mobile_number", userPhoneNo)
                    params.put("password", confirmPwd)
                    params.put("otp", otpStr)

                    val url = "http://13.235.250.119/v2/reset_password/fetch_result/"
                    val queue = Volley.newRequestQueue(this@ResetPassword)
                    val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, params,
                    Response.Listener {
                        try{
                            val json = it.getJSONObject("data")
                            val success = json.getBoolean("success")
                            if(success) {
                                val intent = Intent(this@ResetPassword, LoginPage::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(this@ResetPassword, "Check the credentials again!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e: Exception) {
                            Toast.makeText(this@ResetPassword, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(this@ResetPassword, "Check your Internet Connection!", Toast.LENGTH_SHORT).show()
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
        otp = findViewById(R.id.id_userOTP)
        password = findViewById(R.id.id_userRPassword)
        confirmPassword = findViewById(R.id.id_userRCPassword)
        confirm = findViewById(R.id.id_confirmButton)
    }
}