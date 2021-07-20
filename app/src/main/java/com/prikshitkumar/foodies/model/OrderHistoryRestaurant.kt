package com.prikshitkumar.foodies.model

data class OrderHistoryRestaurant(
        val order_Id: String,
        val restaurant_Name: String,
        val total_Cost: String,
        val order_Time: String,
        val itemList: ArrayList<ChildHistoryModel>
)
