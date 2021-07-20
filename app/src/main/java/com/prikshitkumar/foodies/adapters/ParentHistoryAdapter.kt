package com.prikshitkumar.foodies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.model.ChildHistoryModel
import com.prikshitkumar.foodies.model.OrderHistoryRestaurant

class ParentHistoryAdapter(val context: Context, val parentList: ArrayList<OrderHistoryRestaurant>): RecyclerView.Adapter<ParentHistoryAdapter.ParentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.parent_recyclerview_history, parent, false)
        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val parentItem = parentList[position]

        holder.restaurantName.text = parentItem.restaurant_Name
        holder.orderTime.text = parentItem.order_Time

        val childHistoryAdapter = ChildHistoryAdapter(context, parentItem.itemList)
        val childLayoutManager = LinearLayoutManager(context)

        holder.recyclerView.layoutManager = childLayoutManager
        holder.recyclerView.adapter = childHistoryAdapter
    }

    override fun getItemCount(): Int {
        return parentList.size
    }

    class ParentViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.id_restaurantNameHistory)
        val orderTime: TextView = view.findViewById(R.id.id_orderDateHistory)
        val recyclerView: RecyclerView = view.findViewById(R.id.id_childRecyclerViewHistory)
    }
}