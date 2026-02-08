package com.example.finamobileapp.models.repository

import com.example.finamobileapp.database.TransactionDao
import com.example.finamobileapp.models.Transaction
import kotlinx.coroutines.flow.Flow

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
}