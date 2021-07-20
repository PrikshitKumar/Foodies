package com.prikshitkumar.foodies.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.activity.LoginPage
import com.prikshitkumar.foodies.adapters.HomeAdapter
import com.prikshitkumar.foodies.database.RestaurantDatabase
import com.prikshitkumar.foodies.database.RestaurantEntity
import com.prikshitkumar.foodies.model.Restaurant
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class Home : Fragment() {

    /* For working with RecyclerView we have to take care of three things:
     *  1) LayoutManager (It manages the arrangement of List Items)
     *  2) ViewHolder    (It holds the View of list Items and use the reference of these view for all other items of List)
     *  3) Adapter       (It bind the data with View or Items of the List and Adapter holds the ViewHolder for this binding work. ViewHolder
                           is also a class)
    */

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerViewAdapter: HomeAdapter
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    var priceComparator = Comparator<Restaurant>{ restaurant1, restaurant2 ->
        if(restaurant1.perPersonPrice.compareTo(restaurant2.perPersonPrice, true) == 0) {
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName, true)
        }
        else {
            restaurant1.perPersonPrice.compareTo(restaurant2.perPersonPrice, true)
        }
    }

    var ratingComparator = Comparator<Restaurant>{ restaurant1, restaurant2 ->
        if(restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating, true) == 0) {
            // If the value of both the object is same.
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName, true)
        }
        else {
            restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating, true)
        }
    } /* For Comparison of two objects/Strings/.. we don't have to write the code by ourself. Because Kotlin provide us the class called
       * "Comparator" which performs all the comparison operations and return the result. */

    val restaurantList = arrayListOf<Restaurant>()


    override fun onCreateView (
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        // "attachToRoot" is "False" because we don't want to attach the View with Activity Permanently.
        setHasOptionsMenu(true)  /* Now we have to tell the Compiler that this toolbar has the menu file inside it. For that we have to use the
        method called: "setHasOptionsMenu" */

        initializeTheFields(view)

        /* Volley is an HTTP library that makes networking for Android apps easier and, most importantly, faster.
        *  We can request to fetch the data or store the data in Server using Volley Library easily.
        *  Volley Library is best for Developers for networking Operations..
        *
        *  Some another methods for fetching and sending the data to Server are:
        *  1) RetroFit (Library)
        *  2) OKHTTP (Library)
        *  3) HttpURLConnection (Method)
        *
        *  We can also specify the form of response of the Server like pdf, String, JSON, etc.. And, this format is specified in the headers
        *  method.
        *  To load the Image from the URL we have to use the library "PICASSO". This library is used to populate the Image to ImageView from
        *  the link.
        */


        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        val queue = Volley.newRequestQueue(activity as Context)  // This variable holds the Responses from API

        try {
            val jsonRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener {
                        /* Here we will handle the Response from Server/API
                         * Now parse the JSON File and put the values in ArrayList that we had created for Book and then send this ArrayList
                         * to the Adapter
                        */
                        val json = it.getJSONObject("data")
                        val success = json.getBoolean("success")
                        if (success) {
                            val data = json.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val restaurantJSONObject = data.getJSONObject(i)
                                val restaurantObject = Restaurant(
                                        restaurantJSONObject.getString("id"),
                                        restaurantJSONObject.getString("name"),
                                        restaurantJSONObject.getString("cost_for_one"),
                                        restaurantJSONObject.getString("image_url"),
                                        restaurantJSONObject.getString("rating")
                                )
                                restaurantList.add(restaurantObject)
                            }
                            recyclerViewAdapter = HomeAdapter(
                                    activity as Context,    // "as" keyword is used for Type-Casting in Kotlin
                                    restaurantList
                            )
                            recyclerView.adapter = recyclerViewAdapter
                            recyclerView.layoutManager = layoutManager

                            progressBarLayout.visibility = View.GONE
                        }
                        else {
                            Toast.makeText(context, "Couldn't load the data", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        // Here we will handle the Errors
                        if(activity != null) {
                            Toast.makeText(activity as Context, "Check your Internet Connection and Open the application again!", Toast.LENGTH_SHORT).show()
                            requireActivity().finishAffinity()
                        }
                    }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>() // HashMap is the subclass of MutableMap Class. So, we can also return this.
                    headers["content_Type"] = "application/JSON"  // Specify the datatype of sending and receiving form of data to/from API.
                    headers["token"] = "9bf534118365f1" // Unique Token (anything but remember this).
                    return headers  // header is sent to the API.
                }
            }
            queue.add(jsonRequest)
        }
        catch(e: JSONException){
            Toast.makeText(activity as Context, "Might be Server Side Issue!!", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    fun initializeTheFields(view: View) {
        recyclerView= view.findViewById(R.id.id_recyclerView)
        layoutManager= LinearLayoutManager(activity as Context)
        progressBarLayout= view.findViewById(R.id.id_progressBarLayout)
        progressBar= view.findViewById(R.id.id_progressBar)
        progressBarLayout.visibility= View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.id_sortMenu) {
            // setup the alert builder
            val builder = AlertDialog.Builder(activity as Context)
            builder.setTitle("Sort By")
            // add a radio button list
            val options = arrayOf<String>("Cost(Low to High)", "Cost(High to Low)", "Rating")
            val checkedItem = 1
            builder.setSingleChoiceItems(options, checkedItem, DialogInterface.OnClickListener { dialog, which ->
            // user checked an item and here 'which' is the position selected
                when (which) {
                    0 -> {
                        Collections.sort(restaurantList, priceComparator)
                    }
                    1 -> {
                        Collections.sort(restaurantList, priceComparator)
                        restaurantList.reverse()
                    }
                    2 -> {
                        Collections.sort(restaurantList, ratingComparator)  /* This method will sort the items according to ratingComparator
                        in ascending order and store the result in restaurantList. */
                        restaurantList.reverse() // For arrange the list into descending Order
                    }
                }
            })
            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                recyclerViewAdapter.notifyDataSetChanged() /* This is for notify the Recycler View Adapter that we change (sort) the data.. */
                Toast.makeText(activity as Context, "List Sorted Successfully!", Toast.LENGTH_SHORT).show()
            })
            builder.setNegativeButton("Cancel", null)

            // create and show the alert dialog
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

        return super.onOptionsItemSelected(item)
    }

}