package ca.myscc.w0847446.expensetrackerapp.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ca.myscc.w0847446.expensetrackerapp.model.ExpenseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseItemDao {
    @Insert
    fun addItem(item: ExpenseItem)

    @Delete
    fun deleteItem(item: ExpenseItem)

    @Query("DELETE FROM ExpenseItem WHERE id=:id")
    fun deleteItem(id: Int)

    @Query("SElECT * FROM ExpenseItem WHERE id=:id")
    fun getItem(id: Int): ExpenseItem
    @Query("SELECT * FROM ExpenseItem")
    fun getList(): Flow<MutableList<ExpenseItem>>
}