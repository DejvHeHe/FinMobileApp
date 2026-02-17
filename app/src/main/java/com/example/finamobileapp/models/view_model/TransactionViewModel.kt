package com.example.finamobileapp.models.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.models.*
import com.example.finamobileapp.models.database.TransactionDatabase
import com.example.finamobileapp.models.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository

    init {
        val transactionDao = TransactionDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
    }


    fun getBalance(date: LocalDate, accountType: TransactionAccountType): Flow<Int> =
        repository.getBalance(date, accountType)

    fun getSumyByType(date: LocalDate): Flow<Map<TransactionType, Int>> =
        repository.getSumyByType(date)

    fun getSumyByCategories(date: LocalDate, type: TransactionType): Flow<Map<TransactionCategory, Int>> =
        repository.getSumyByCategories(date, type)

    fun getSumForCategory(date: LocalDate, category: TransactionCategory): Flow<Int> =
        repository.getSumForCategory(date, category)

    fun getTransactions(date: LocalDate, category: TransactionCategory): Flow<List<Transaction>> =
        repository.getTransactions(date, category)



    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSmartTransaction(transaction)
        }
    }

    fun addRecurring(transaction: Transaction, endDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecurringTransactions(transaction, endDate)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteRecurring(groupId: String, today: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRecurring(groupId, today)
        }
    }

    fun updateRecurring(groupId: String, name: String, amount: Int, category: TransactionCategory, type: TransactionType, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRecurring(groupId, name, amount, category, type, description)
        }
    }
}