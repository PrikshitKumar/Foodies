package com.prikshitkumar.foodies.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.adapters.MenuAdapter
import com.prikshitkumar.foodies.model.Menu
import java.lang.reflect.Type

class MenuRestaurant : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerViewAdapter: MenuAdapter
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var toolBar: Toolbar
    lateinit var addToCart: Button
    var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_restaurant)

        initializeTheFields()

        val restaurant_id: String? = intent.getStringExtra("restaurant_id")
        title = intent.getStringExtra("Title")

        setUpToolbar()
        val dishList = arrayListOf<Menu>()

        addToCart.setOnClickListener {
            val foodOrder: SharedPreferences = getSharedPreferences("foodOrder", MODE_PRIVATE)
            val totalPrice = foodOrder.getString("total_cost", " ")
            val queue = foodOrder.getString("food", " ")

            if(totalPrice?.toInt() == 0) {
                Toast.makeText(this, "Before Place Order, add the items to cart.", Toast.LENGTH_SHORT).show()
            }
            else {
                val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
                val editor: SharedPreferences.Editor = foodOrder.edit()
                editor.putString("restaurant_id", restaurant_id)
                editor.putString("restaurant_name", title)
                editor.putString("user_id", sharedPreferences.getString("user_id", " "))
                editor.apply()

//                Toast.makeText(this, "Total Bill: $totalPrice", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "Items: ${queue.toString()}", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@MenuRestaurant, CartItems::class.java)
                intent.putExtra("food", queue)
                intent.putExtra("total_cost", totalPrice)
                startActivity(intent)
                onBackPressed() /* Instead of calling finish(), I call this method. In this method, I wrote the code for update the list to
                 null and totalPrice to 0. So, when we again place the order, previous data is not conflicted or not make any error. */
            }
        }

        val url= " http://13.235.250.119/v2/restaurants/fetch_result/${restaurant_id}/"
        val queue = Volley.newRequestQueue(this@MenuRestaurant)

        try {
            val jsonRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {

                    val json = it.getJSONObject("data")
                    val success = json.getBoolean("success")
                    if (success) {
                        val data = json.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val jsonObject = data.getJSONObject(i)
                            val menuItems = Menu (
                                jsonObject.getString("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("cost_for_one"),
                                jsonObject.getString("restaurant_id")
                            )
                            dishList.add(menuItems)
                        }
                        recyclerViewAdapter = MenuAdapter (
                                this@MenuRestaurant,
                                dishList
                        )

                        recyclerView.adapter = recyclerViewAdapter
                        recyclerView.layoutManager = layoutManager
                        progressBarLayout.visibility = View.GONE
                        addToCart.visibility = View.VISIBLE

                    }
                    else {
                        Toast.makeText(this@MenuRestaurant,"Couldn't load the data", Toast.LENGTH_SHORT ).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this@MenuRestaurant, "Check your Internet Connection!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["content_Type"] = "application/JSON"
                    headers["token"] = "9bf534118365f1"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
        catch(e: Exception) {
            Toast.makeText(this@MenuRestaurant, "Might be Server Side Issue!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun initializeTheFields() {
        recyclerView = findViewById(R.id.id_recyclerViewMenu)
        layoutManager = LinearLayoutManager(this@MenuRestaurant)
        progressBarLayout = findViewById(R.id.id_progressBarLayoutMenu)
        progressBar = findViewById(R.id.id_progressBarMenu)
        progressBarLayout.visibility = View.VISIBLE
        toolBar = findViewById(R.id.id_toolbarMenu)
        addToCart = findViewById(R.id.id_addTOCartButtonMenu)
        addToCart.visibility = View.GONE
    }

    fun setUpToolbar() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = title
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home) {
            updateData()
            finish()
        }
        return  super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        updateData()
        finish()
        super.onBackPressed()
    }

    fun updateData() {
        val foodOrder: SharedPreferences = getSharedPreferences("foodOrder", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = foodOrder.edit()
        editor.putString("total_cost", "0")
        editor.putString("food", null)
        editor.apply()
    }
}