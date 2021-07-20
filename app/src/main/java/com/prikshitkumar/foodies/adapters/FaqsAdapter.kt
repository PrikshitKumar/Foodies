package com.prikshitkumar.foodies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.model.FaqsModel

class FaqsAdapter(val context: Context, val list: ArrayList<FaqsModel>): RecyclerView.Adapter<FaqsAdapter.FaqsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqsViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.faqs_items_layout, parent, false)
        return FaqsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqsViewHolder, position: Int) {
        val temp = list[position]
        holder.question.text = temp.question
        holder.answer.text = temp.answer
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class FaqsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val question: TextView = view.findViewById(R.id.id_FAQs_Question)
        val answer: TextView = view.findViewById(R.id.id_FAQs_Answer)
    }
}
