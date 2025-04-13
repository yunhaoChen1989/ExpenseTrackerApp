package ca.myscc.w0847446.expensetrackerapp.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.myscc.w0847446.expensetrackerapp.adapter.CurrencyConverter
import ca.myscc.w0847446.expensetrackerapp.dao.ExpenseItemDao
import ca.myscc.w0847446.expensetrackerapp.model.ExpenseItem

@Database(
    entities = [ExpenseItem::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(CurrencyConverter::class) // Add TypeConverters
abstract class ExpenseItemDatabase: RoomDatabase() {
    abstract val expenseItemDao: ExpenseItemDao
}