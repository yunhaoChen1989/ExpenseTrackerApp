package ca.myscc.w0847446.expensetrackerapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecycleAdapter(var expenseList: MutableList<ExpenseItem>): RecyclerView.Adapter<RecycleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item_view, parent, false)

        return RecycleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.apply {
            nameItem.text = expenseList[position].name
            amountItem.text = expenseList[position].amount.toString()
            deleteButton.setOnClickListener {
                expenseList.removeAt(position)
            }
        }
    }
}