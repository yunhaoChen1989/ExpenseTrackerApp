package ca.myscc.w0847446.expensetrackerapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.myscc.w0847446.expensetrackerapp.model.ExpenseItem

class ExpenseListViewModel: ViewModel() {
    var expenseList =MutableLiveData<MutableList<ExpenseItem>>()

    fun getList(): MutableList<ExpenseItem> {
        return expenseList.value?:mutableListOf<ExpenseItem>()
    }
    fun deleteItem(position: Int){
        expenseList.value = expenseList.value?.toMutableList()?.also { it.removeAt(position) }
    }
    fun addItem(item: ExpenseItem){
        //expenseList.value?.add(item)
        val updatedList = expenseList.value.orEmpty().toMutableList()
        updatedList.add(item)
        expenseList.value = updatedList // This triggers UI update
    }

    fun getItem(position: Int): ExpenseItem?{
        return expenseList.value?.get(position)
    }

    fun loadList(items: MutableList<ExpenseItem>){
        expenseList.value = items
    }
}