package ca.myscc.w0847446.expensetrackerapp.model

import android.icu.util.Currency
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ExpenseItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val amount: Double,
    val date: String,
    val currency: Currency,
    val convertedCost: Double,
    val costAssociated: Boolean=false):Serializable {
}