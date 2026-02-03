package com.example.finamobileapp.database

import androidx.room.*
import com.example.finamobileapp.models.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun addTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions_table ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    // Pro možnost smazání transakce
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
}