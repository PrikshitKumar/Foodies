package com.prikshitkumar.foodies.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    fun insertData(restaurantEntity: RestaurantEntity) /* Room Library will take care of these two functions insert the Data and delete the data.
        So, we don't need to add the Queries by our self here. And for all other operations we need to write the Query. As we use the annotation
        like @Insert, @Delete. Because of these annotation the insertion and deletion actions are perform automatically. That's why we don't
        need to write any query here... */

    @Delete
    fun deleteData(restaurantEntity: RestaurantEntity)

    @Query("select * from restaurants") /* This is how we can write the Queries. "restaurants is the table name". */
    fun getAllData(): List<RestaurantEntity> /* Now, for get the restaurants from database we have to write this code for this function. */

    @Query("select * from restaurants where restaurant_Id = :restaurantId") /* : is used for telling the compiler that the value is came
      from the function that is just below the Query. */
    fun getDataById(restaurantId: String): RestaurantEntity
}

/* Here all the functions has only declaration not the function body. Because all the operation performed in the database class and taken care
* by the ROOM Library. Hence, we don't need to gave the implementation to these functions. That's why DAO is the Interface not the class.. */