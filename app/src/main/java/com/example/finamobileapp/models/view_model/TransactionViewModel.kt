package com.example.finamobileapp.models.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.database.TransactionDatabase
import com.example.finamobileapp.models.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransactionViewModel(appliacation:Application):AndroidViewModel(appliacation) {

    private val getAllTransactions : Flow<List<Transaction>>
    private val repository: TransactionRepository

    init{
        val transactionDao=TransactionDatabase.getDatabase(appliacation).transactionDao()
        repository=TransactionRepository(transactionDao)
        getAllTransactions=repository.allTransactions
    }
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTransaction(transaction)
        }
    }



}