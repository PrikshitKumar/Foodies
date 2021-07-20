package com.prikshitkumar.foodies.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.adapters.CartAdapter
import com.prikshitkumar.foodies.model.CartModel
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class CartItems : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerViewAdapter: CartAdapter
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var toolBar: Toolbar
    lateinit var name: TextView
    lateinit var placeOrder: Button
    lateinit var tempItemsData: String
    lateinit var totalCost: String

    var list = arrayListOf<CartModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_items)

        tempItemsData = intent.getStringExtra("food").toString()
        totalCost = intent.getStringExtra("total_cost").toString()

        initializeTheFields()
        setUpToolbar()
        getTheOrderData()

        val foodOrder: SharedPreferences = getSharedPreferences("foodOrder", MODE_PRIVATE)
        name.text = "Ordering From:  ${foodOrder.getString("restaurant_name", " ")?.toUpperCase()}"

        val jsonArray = JSONArray()
        for(i in 0 until list.size) {
            val jsonObject = JSONObject()
            jsonObject.put("food_item_id", list[i].food_item_id)
            jsonArray.put(jsonObject)
        }

        val params = JSONObject()
        params.put("user_id", foodOrder.getString("user_id", " "))
        params.put("restaurant_id", foodOrder.getString("restaurant_id", " "))
        params.put("total_cost", totalCost)
        params.put("food",jsonArray)

        val url = "http://13.235.250.119/v2/place_order/fetch_result/"
        val queue = Volley.newRequestQueue(this@CartItems)

        recyclerViewAdapter = CartAdapter(this@CartItems, list)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = layoutManager
        progressBarLayout.visibility = View.GONE
        placeOrder.visibility = View.VISIBLE
        Toast.makeText(this, "Total Payable Amount: $totalCost Rs.", Toast.LENGTH_LONG).show()

        placeOrder.setOnClickListener {
            try {
                val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, params,
                        Response.Listener {
                            val json = it.getJSONObject("data")
                            val success = json.getBoolean("success")
                            if(success) {
                                // Means Data is successfully placed.
                                val intent = Intent(this@CartItems, ConfirmationPage::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(this, "Couldn't load the data", Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            Toast.makeText(this, "Check your Internet Connection!", Toast.LENGTH_SHORT).show()
                            finish()
                        }) {
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
                Toast.makeText(this, "Might be Server Side Issue!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun initializeTheFields() {
        recyclerView = findViewById(R.id.id_recyclerViewCart)
        layoutManager = LinearLayoutManager(this@CartItems)
        progressBarLayout = findViewById(R.id.id_progressBarLayoutCart)
        progressBar = findViewById(R.id.id_progressBarCart)
        toolBar = findViewById(R.id.id_toolbarCart)
        placeOrder = findViewById(R.id.id_placeOrderCart)
        name = findViewById(R.id.id_txtCart)

        progressBarLayout.visibility = View.VISIBLE
        placeOrder.visibility = View.GONE
    }

    fun setUpToolbar() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getTheOrderData() {
        val gson = Gson()
//        val type = object : TypeToken<ArrayList<CartModel>>(){}.type
//        list = gson.fromJson(tempItemsData, type)
        list.addAll(gson.fromJson(tempItemsData, Array<CartModel>::class.java).asList())
//        Toast.makeText(this, list.toString(), Toast.LENGTH_SHORT).show()
    }
}