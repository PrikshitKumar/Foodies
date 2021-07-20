package com.prikshitkumar.foodies.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.model.CartModel
import com.prikshitkumar.foodies.model.Menu
import org.json.JSONObject

class MenuAdapter(val context: Context, val dishList: ArrayList<Menu>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    val orderList = arrayListOf<CartModel>()
    var totalPrice: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_items_layout, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = dishList[position]

        holder.srno.text = menu.dishId
        holder.dishName.text = menu.dishName
        holder.dishPrice.text = "${menu.dishPrice} Rs."

        holder.button.setOnClickListener {
            if(holder.temp) {
                holder.temp = false
                holder.button.text = "Remove"
                holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
                orderList.add(CartModel(menu.dishId, menu.dishName, menu.dishPrice))
                totalPrice += menu.dishPrice.toInt()
                orderData()
            }
            else {
                holder.temp = true
                holder.button.text = "Add"
                holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
                orderList.remove(CartModel(menu.dishId, menu.dishName, menu.dishPrice))
                totalPrice -= menu.dishPrice.toInt()
                orderData()
            }
        }
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    class MenuViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val srno: TextView = view.findViewById(R.id.id_srnoMenu)
        val dishName: TextView = view.findViewById(R.id.id_dishNameMenu)
        val dishPrice: TextView = view.findViewById(R.id.id_dishPriceMenu)
        val button: Button = view.findViewById(R.id.id_addRemoveButtonMenu)
        var temp: Boolean = true
    }

    fun orderData() {
        val foodOrder: SharedPreferences = context.getSharedPreferences("foodOrder", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = foodOrder.edit()
        editor.putString("total_cost", totalPrice.toString())
        val gson = Gson()
        /* With the help of Gson Library, we can change any type of Data to and from Json easily. */
        val foodItems: String = gson.toJson(orderList)
        editor.putString("food", foodItems)
        editor.apply()
    }
}