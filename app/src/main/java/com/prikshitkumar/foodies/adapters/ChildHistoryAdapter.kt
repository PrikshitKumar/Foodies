package com.prikshitkumar.foodies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.model.ChildHistoryModel

class ChildHistoryAdapter(val context: Context, val childHistoryList: ArrayList<ChildHistoryModel>) : RecyclerView.Adapter<ChildHistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_items_layout, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val order = childHistoryList[position]

        holder.dishName.text = order.dish_Name
        holder.dishPrice.text = "Rs. ${order.dish_Price}"

    }

    override fun getItemCount(): Int {
        return childHistoryList.size
    }

    class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dishName: TextView = view.findViewById(R.id.id_dishNameCart)
        val dishPrice: TextView = view.findViewById(R.id.id_dishPriceCart)
    }
}
