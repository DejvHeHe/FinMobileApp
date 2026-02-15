package com.example.finamobileapp.models.repository

import com.example.finamobileapp.database.TransactionDao
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TransactionRepository(private val transactionDao: TransactionDao) {


    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()


    suspend fun addTransaction(transaction: Transaction) {
        transactionDao.addTransaction(transaction)
    }


    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
    suspend fun updateTransaction(transaction: Transaction)
    {
        transactionDao.updateTransaction(transaction)
    }
    suspend fun deleteRecurring(groupId:String,today: LocalDate)
    {
        transactionDao.deleteRecurring(groupId,today)
    }
    suspend fun updateRecurring(groupId: String, name: String, amount: Int, category: TransactionCategory, type: TransactionType, description: String)
    {
        transactionDao.updateRecurring(groupId,name,amount,category,type,description)
    }
}