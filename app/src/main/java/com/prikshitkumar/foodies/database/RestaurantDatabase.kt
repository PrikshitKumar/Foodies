package com.prikshitkumar.foodies.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class], version = 1)
/* In the attributes of Database annotation, we have to pass the entities or tables those we need in this class. The reference of RestaurantEntity
    class is stored in entities attribute. 2nd parameter is the version of the database. It's important to mention the version. Because after
    sometime, if we want to update our application or its code at the latest stage. In that case we have to convert the version of the database.
    So that, the app won't be crash if user updates the app to the newer version. And, the version number is always the natural number (1,2,
    3,....). Next we need to tell here all the functions, that we will perform on the data and these actions will be performed by the DAO
    Interface..
*/

abstract class RestaurantDatabase: RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao
    /* Return type of this function is RestaurantDao. So, it returns the Dao Value and this return will allow us to use all the functionality of
       the DAO Interface.
       Example, when we have any String Type value then we can perform all the functions of String on it, like length, indexOf, etc.. Similarly
       this function has the type Database by parent and also the return type is DAO. Thus, we will be able to use all the function of DAO on the
       value that it will return.
       This function will serve as the Doorway for all the DAO Operations. And, because of abstract class there's not the default implementation
       of this function.
    */
}