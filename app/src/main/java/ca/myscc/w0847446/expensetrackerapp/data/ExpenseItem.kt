package ca.myscc.w0847446.expensetrackerapp.data

import android.icu.util.Currency
import java.io.Serializable

class ExpenseItem(val name: String, val amount: Double, val date: String, val currency: Currency, val convertedCost: Double):Serializable {
}