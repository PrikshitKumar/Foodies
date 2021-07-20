package com.prikshitkumar.foodies.adapters

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.activity.MenuRestaurant
import com.prikshitkumar.foodies.database.RestaurantDatabase
import com.prikshitkumar.foodies.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavoriteAdapter(val context: Context, val restaurantList: List<RestaurantEntity>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    lateinit var restaurantEntityTemp: RestaurantEntity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_items_layout, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val restaurant= restaurantList[position]
        holder.restaurantName.text = restaurant.restaurant_Name
        holder.restaurantPrice.text = "${restaurant.per_Person_Price} Rs."
        holder.restaurantRating.text = restaurant.restaurant_Rating
        Picasso.get().load(restaurant.restaurant_Image).error(R.drawable.error).into(holder.restaurantImage)
        holder.restaurantHeart.setImageResource(R.drawable.icon_heart)

        restaurantEntityTemp = RestaurantEntity(
                restaurant.restaurant_Id,
                restaurant.restaurant_Name,
                restaurant.per_Person_Price,
                restaurant.restaurant_Image,
                restaurant.restaurant_Rating
        )

        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, MenuRestaurant::class.java)
            intent.putExtra("restaurant_id", restaurant.restaurant_Id.toString())
            intent.putExtra("Title", restaurant.restaurant_Name)
            context.startActivity(intent)
        }
        holder.restaurantHeart.setOnClickListener {
            if(holder.temp) {
                val result = updateFavorites(context, restaurantEntityTemp, 1).execute().get()
                if(result) {
                    holder.temp = false
                    holder.restaurantHeart.setImageResource(R.drawable.icon_heart)
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                val result = updateFavorites(context, restaurantEntityTemp, 2).execute().get()
                if(result) {
                    holder.temp = true
                    holder.restaurantHeart.setImageResource(R.drawable.icon_favorite)
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class FavoriteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.id_restaurantName)
        val restaurantPrice: TextView = view.findViewById(R.id.id_perPersonPrice)
        val restaurantImage: ImageView = view.findViewById(R.id.id_restaurantImage)
        val restaurantRating: TextView = view.findViewById(R.id.id_restaurantRating)
        val restaurantHeart: ImageView = view.findViewById(R.id.id_restaurantHeart)
        val itemLayout: LinearLayout = view.findViewById(R.id.id_recylerView_Items_UI)
        var temp: Boolean = false
    }

    class updateFavorites(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int): AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode) {
                1 -> {
                    // Save the book into DB as favorite
                    db.restaurantDao().insertData(restaurantEntity)
                    db.close()
                    return true
                }
                2 -> {
                    // Remove the book from favorite.
                    db.restaurantDao().deleteData(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}
