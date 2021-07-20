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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.activity.MenuRestaurant
import com.prikshitkumar.foodies.database.RestaurantDatabase
import com.prikshitkumar.foodies.database.RestaurantEntity
import com.prikshitkumar.foodies.model.Restaurant
import com.squareup.picasso.Picasso

class HomeAdapter(val context: Context, val itemList: ArrayList<Restaurant>): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
    /* If we have 100 items to show in the list. Then, this method shown the number of items that much fits in the user screen and uses those
     *  view for next items. All this work is handled in this method */
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_items_layout, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
    /* This method is responsible for the recycling and reusing of the ViewHolders. This method also takes care of data goes to the correct position. */
        val restaurant= itemList[position]

        holder.restaurantName.text = restaurant.restaurantName
        holder.restaurantPrice.text = "${restaurant.perPersonPrice} Rs."
        holder.restaurantRating.text = restaurant.restaurantRating
        /* To load the Image from the URL we have to use the library "PICASSO" This library is used to populate the Image to ImageView from the link. */
        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.error).into(holder.restaurantImage)

        val restaurantEntity = RestaurantEntity (
                restaurant.restaurantId.toInt(),
                restaurant.restaurantName,
                restaurant.perPersonPrice,
                restaurant.restaurantImage,
                restaurant.restaurantRating
        )

        val checkFavorites = DBAsyncTask(context, restaurantEntity, 1).execute() // execute() is for start the binded action
        val isFavorite = checkFavorites.get() /* get() method will return the result of background process, whether the actions is successfully
                   * executed or not and returns the Boolean value.. */

        if(isFavorite) {
            holder.restaurantHeart.setImageResource(R.drawable.icon_heart)
        }
        else {
            holder.restaurantHeart.setImageResource(R.drawable.icon_favorite)
        }

        holder.itemLayout.setOnClickListener {
            val intent = Intent(context, MenuRestaurant::class.java)
            intent.putExtra("restaurant_id", restaurant.restaurantId)
            intent.putExtra("Title", restaurant.restaurantName)
            context.startActivity(intent)
        }

        holder.restaurantHeart.setOnClickListener {
            if(!DBAsyncTask(context, restaurantEntity, 1).execute().get()) {
                val async = DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if(result) {
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.icon_heart)
                }
                else {
                    Toast.makeText(context, "Something went Wrong!", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                val async = DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()
                if(result) {
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.icon_favorite)
                }
                else {
                    Toast.makeText(context, "Something went Wrong!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        /* This method stores the total number of items of the List */
        return itemList.size
    }

    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val restaurantName: TextView= view.findViewById(R.id.id_restaurantName)
        val restaurantPrice: TextView= view.findViewById(R.id.id_perPersonPrice)
        val restaurantImage: ImageView= view.findViewById(R.id.id_restaurantImage)
        val restaurantRating: TextView= view.findViewById(R.id.id_restaurantRating)
        val restaurantHeart: ImageView = view.findViewById(R.id.id_restaurantHeart)
        val itemLayout: LinearLayout= view.findViewById(R.id.id_recylerView_Items_UI)
    }



    /* This class is already inherited the AppCompatActivity class. And, in kotlin we can not inherit more than one class. That's why
     * we declare and initialize the class here (Nested class).
     * As we know AsyncTask needs the three parameters: 1) params, 2) Progress, 3) Results. But we only have to know that whether the applied
     * is successfully completed or not. So, we don't have to pass the 1st two values to the attributes.
     * AsyncTask has 4 methods:
     * 1) onPreExecute(), 2) doInBackground(), 3) onProgressUpdate(), 4) onPostExecute().
     * doInBackground() method is mandatory to implement.
     *
     * Whenever we are using this AsyncTask class, we have to initialize the database class. And, if we want to perform database operations,
     * then we need the context, that tells which part of the app made the request for accessing the resources. Also, these operations are
     * used either for save the restaurant to favorites or delete the restaurant from favorites. Means, this class will also need one more
     * parameter for RestaurantEntity.
     *
     * This class "DBAsyncTask" needs to perform three operations:
     * 1) Check the database whether the restaurant is already added to favorites or not.
     * 2) Add a restaurant to the favorites.
     * 3) Remove a restaurant from the favorites. */

    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {
        /* Mode 1 -> Check DB if the book is in favorite or not.
         * Mode 2 -> Save the book into DB as favorite.
         * Mode 3 -> Remove the book from favorite. */

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build() // Initialization of Database
        /* databaseBuilder method needs three parameters.
             * 1st: context, 2nd: Java version of the class which is responsible for create the database, 3rd: Name of the Database.
             * build() is to build the database properly.
             * Note: It is always mandatory to close the database after performing the operations. Otherwise it takes the unnecessary memory. */

        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode) {
                1 -> {
                    /* Check DB whether the restaurant is in favorite or not. For it, we need to check the id of the restaurant whether that it
                     * is present in database or not. */
                    val restaurant: RestaurantEntity = db.restaurantDao().getDataById(restaurantEntity.restaurant_Id.toString())
                    db.close()
                    return restaurant != null  //it returns true of value of book is not null, otherwise returns false, if the value is null.
                }
                2 -> {
                    // Save the restaurant into DB as favorite
                    db.restaurantDao().insertData(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    // Remove the restaurant from favorite.
                    db.restaurantDao().deleteData(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}
