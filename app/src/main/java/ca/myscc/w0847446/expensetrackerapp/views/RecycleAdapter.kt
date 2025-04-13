package ca.myscc.w0847446.expensetrackerapp.views

import android.content.Context
import android.icu.util.Currency
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import ca.myscc.w0847446.expensetrackerapp.R
import ca.myscc.w0847446.expensetrackerapp.model.ExpenseItem
import ca.myscc.w0847446.expensetrackerapp.fragments.MainFragment
import ca.myscc.w0847446.expensetrackerapp.viewModel.ExpenseListViewModel

class RecycleAdapter(private val activity: MainFragment, private val context: Context, var expenseList: MutableList<ExpenseItem>): RecyclerView.Adapter<RecycleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item_view, parent, false)

        return RecycleViewHolder(view)
    }

    fun updateList(eList: MutableList<ExpenseItem>){
        expenseList = eList
        notifyItemRemoved(expenseList.count())
    }
    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.apply {
            val item = expenseList[position]
            //?: ExpenseItem("null", 0.0,"2025-4-16", Currency.getInstance("CAD"), 0.0,false)

            nameItem.text = item.name
            amountItem.text = item.amount.toString()
            val currency = item.currency
            associatedAmount.text = "${currency?.symbol?:""}${item.convertedCost?:0.0}"
            deleteButton.setOnClickListener {
                activity.deleteItem(item.id)
                notifyItemRemoved(position)
                //notifyDataSetChanged()
                //activity.updateTotalExpense()
                //activity.saveListToFile(context)
            }
            showDetail.setOnClickListener {
                //val item = expenseList[position]
                // Create intent to start next activity
                //val intent = Intent(context, ExpenseDetailsActivity::class.java)
                //intent.putExtra("DETAIL", item) // add the data

                // Start next activity
                //context.startActivity(intent)
                activity.showDetails(item.id)
            }
        }
    }
}