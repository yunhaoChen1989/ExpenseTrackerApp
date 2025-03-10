package ca.myscc.w0847446.expensetrackerapp

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class RecycleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    lateinit var nameItem: TextView
    lateinit var amountItem: TextView
    lateinit var deleteButton: Button
    lateinit var showDetail: Button

    init {
        nameItem = itemView.findViewById(R.id.name_item)
        amountItem = itemView.findViewById(R.id.amount_item)
        deleteButton = itemView.findViewById(R.id.delete_item)
        showDetail = itemView.findViewById(R.id.showDetail)
    }
}