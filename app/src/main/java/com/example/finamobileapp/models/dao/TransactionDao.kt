package com.example.finamobileapp.database

import androidx.room.*
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.TransactionAccountType
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionDao {

    @Insert
    suspend fun addTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions_table ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>


    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Update
    suspend fun  updateTransaction(transaction: Transaction)


    @Query("""
    UPDATE transactions_table 
    SET name = :name, 
        amount = :amount, 
        category = :category, 
        type = :type,
        description = :description
    WHERE groupId = :groupId
""")
    suspend fun updateRecurring(
        groupId: String,
        name: String,
        amount: Int,
        category: TransactionCategory,
        type: TransactionType,
        description: String
    )

    @Query("DELETE FROM transactions_table WHERE groupId = :groupId AND date >= :today")
    suspend fun deleteRecurring(groupId: String,today: LocalDate)
}