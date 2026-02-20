package com.example.finamobileapp.models.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.models.BuyIdeas
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.TransactionAccountType
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.TransactionType
import com.example.finamobileapp.models.database.TransactionDatabase
import com.example.finamobileapp.models.repository.BuyIdeasRepository
import com.example.finamobileapp.models.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository
    private val buyIdeaRepository: BuyIdeasRepository

    init {
        val transactionDao = TransactionDatabase.getDatabase(application).transactionDao()
        val buyIdeaDao = TransactionDatabase.getDatabase(application).buyIdeaDao()
        transactionRepository = TransactionRepository(transactionDao)
        buyIdeaRepository = BuyIdeasRepository(buyIdeaDao)
    }


    fun getBalance(date: LocalDate, accountType: TransactionAccountType): Flow<Int> =
        transactionRepository.getBalance(date, accountType)

    fun getSumyByType(date: LocalDate): Flow<Map<TransactionType, Int>> =
        transactionRepository.getSumyByType(date)

    fun getSumyByCategories(
        date: LocalDate,
        type: TransactionType
    ): Flow<Map<TransactionCategory, Int>> =
        transactionRepository.getSumyByCategories(date, type)

    fun getSumForCategory(date: LocalDate, category: TransactionCategory): Flow<Int> =
        transactionRepository.getSumForCategory(date, category)

    fun getTransactions(date: LocalDate, category: TransactionCategory): Flow<List<Transaction>> =
        transactionRepository.getTransactions(date, category)


    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.addSmartTransaction(transaction)
        }
    }

    fun addRecurring(transaction: Transaction, endDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.addRecurringTransactions(transaction, endDate)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.deleteTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.updateTransaction(transaction)
        }
    }

    fun deleteRecurring(groupId: String, today: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.deleteRecurring(groupId, today)
        }
    }

    fun updateRecurring(
        groupId: String,
        name: String,
        amount: Int,
        category: TransactionCategory,
        type: TransactionType,
        description: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.updateRecurring(
                groupId,
                name,
                amount,
                category,
                type,
                description
            )
        }
    }

    fun addBuyIdea(buyIdea: BuyIdeas) {
        viewModelScope.launch(Dispatchers.IO) {
            buyIdeaRepository.addBuyIdea(buyIdea)
        }

    }
}