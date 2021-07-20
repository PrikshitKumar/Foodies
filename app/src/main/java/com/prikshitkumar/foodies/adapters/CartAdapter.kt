package com.prikshitkumar.foodies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.model.CartModel

class CartAdapter(val context: Context, val orderList: ArrayList<CartModel>): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_items_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val data = orderList[position]
        holder.dishName.text = data.name
        holder.dishPrice.text = "${data.cost} Rs."
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dishName: TextView = view.findViewById(R.id.id_dishNameCart)
        val dishPrice: TextView = view.findViewById(R.id.id_dishPriceCart)
    }
}