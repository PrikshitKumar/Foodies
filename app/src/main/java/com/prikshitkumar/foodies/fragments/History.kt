package com.prikshitkumar.foodies.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.adapters.ParentHistoryAdapter
import com.prikshitkumar.foodies.model.ChildHistoryModel
import com.prikshitkumar.foodies.model.OrderHistoryRestaurant
import java.lang.Exception

class History : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerViewAdapter: ParentHistoryAdapter
    lateinit var progressBarLayout: RelativeLayout
    lateinit var ntsLayout: RelativeLayout

    val restaurantHistory = arrayListOf<OrderHistoryRestaurant>()

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)
        initializeTheFields(view)

        val sharedPreferences: SharedPreferences? = context?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getString("user_id", " ")

        val url = "http://13.235.250.119/v2/orders/fetch_result/$userId"
        val queue = Volley.newRequestQueue(activity as Context)

        try {
            val jsonRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener {
                        val json = it.getJSONObject("data")
                        val success = json.getBoolean("success")
                        if(success) {
                            val data = json.getJSONArray("data")
                            for(i in 0 until data.length()) {
                                val dishItemsHistory = arrayListOf<ChildHistoryModel>()

                                val itemsHistory = data.getJSONObject(i).getJSONArray("food_items")
                                for(j in 0 until itemsHistory.length()) {
                                    val itemsData = itemsHistory.getJSONObject(j)
                                    val dishData = ChildHistoryModel (
                                            itemsData.getString("food_item_id"),
                                            itemsData.getString("name"),
                                            itemsData.getString("cost")
                                    )
                                    dishItemsHistory.add(dishData)
                                }

                                val history = data.getJSONObject(i)
                                val restaurantData = OrderHistoryRestaurant (
                                        history.getString("order_id"),
                                        history.getString("restaurant_name"),
                                        history.getString("total_cost"),
                                        history.getString("order_placed_at"),
                                        dishItemsHistory
                                )
                                restaurantHistory.add(restaurantData)
                            }

                            recyclerViewAdapter = ParentHistoryAdapter(
                                    activity as Context,
                                    restaurantHistory
                            )
                            recyclerView.adapter = recyclerViewAdapter
                            recyclerView.layoutManager = layoutManager
                            if(restaurantHistory.size == 0) {
                                ntsLayout.visibility = View.VISIBLE
                            }
                            else {
                                ntsLayout.visibility = View.GONE
                            }

                            progressBarLayout.visibility = View.GONE
                            /* RecyclerView have the method addItemDecoration for decorate the items such as adding divider between the items, etc... */
                            recyclerView.addItemDecoration (
                                    DividerItemDecoration(
                                            recyclerView.context,
                                            (layoutManager as LinearLayoutManager).orientation
                                    )
                            )
                        }
                        else {
                            Toast.makeText(activity as Context,"Couldn't load the data", Toast.LENGTH_SHORT ).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(activity as Context, "Check your Internet Connection!", Toast.LENGTH_SHORT).show()
                        requireActivity().finish()
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
        catch (e: Exception) {
            Toast.makeText(activity as Context, "Might be Server Side Issue!!", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    fun initializeTheFields(view: View) {
        recyclerView = view.findViewById(R.id.id_recyclerViewHistory)
        layoutManager = LinearLayoutManager(activity as Context)
        progressBarLayout = view.findViewById(R.id.id_progressBarLayoutHistory)
        ntsLayout = view.findViewById(R.id.id_NTSHistory)
        progressBarLayout.visibility = View.VISIBLE
        ntsLayout.visibility = View.GONE
    }
}