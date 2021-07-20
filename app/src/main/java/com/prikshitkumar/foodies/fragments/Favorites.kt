package com.prikshitkumar.foodies.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.adapters.FavoriteAdapter
import com.prikshitkumar.foodies.database.RestaurantDatabase
import com.prikshitkumar.foodies.database.RestaurantEntity

class Favorites : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerViewAdapter: FavoriteAdapter
    lateinit var emptyLayout: RelativeLayout
    var dbRestaurantList = listOf<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        initializeTheFields(view)

        dbRestaurantList = RetrieveFavorites(activity as Context).execute().get()
        if(activity != null && dbRestaurantList.isNotEmpty()) {
            emptyLayout.visibility = View.GONE
            recyclerViewAdapter = FavoriteAdapter(activity as Context, dbRestaurantList)
            recyclerView.adapter = recyclerViewAdapter
            recyclerView.layoutManager = layoutManager
        }
        else {
            emptyLayout.visibility = View.VISIBLE
        }
        return view
    }

    fun initializeTheFields(view: View) {
        recyclerView= view.findViewById(R.id.id_recyclerViewFavorite)
        layoutManager= LinearLayoutManager(activity as Context)
        emptyLayout= view.findViewById(R.id.id_emptyLayoutFavorite)
        emptyLayout.visibility= View.GONE
    }

    /* Before initialize the Adapter we have to retrieve the list of restaurants from database. Since we created Dao for retrieving all the data,
     * returns the list of restaurantEntity. So, the return type for doInBackground will the restaurantEntity */
    class RetrieveFavorites(val context: Context): AsyncTask<Void, Void, List<RestaurantEntity>>() {
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()
            return db.restaurantDao().getAllData()
        }
    }
}