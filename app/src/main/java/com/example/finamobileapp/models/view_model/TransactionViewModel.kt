package com.example.finamobileapp.models.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.TransactionType
import com.example.finamobileapp.models.database.TransactionDatabase
import com.example.finamobileapp.models.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class TransactionViewModel(application:Application):AndroidViewModel(application) {

    private val allTransactions : Flow<List<Transaction>>
    private val repository: TransactionRepository

    init{
        val transactionDao=TransactionDatabase.getDatabase(application).transactionDao()
        repository=TransactionRepository(transactionDao)
        allTransactions=repository.allTransactions
    }
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTransaction(transaction)
        }
    }

    fun getBalance(date: LocalDate): Flow<Int> {
        return fillterByMonth(date).map { list ->
            list.sumOf {
                if (it.type == TransactionType.INCOME) it.amount else -it.amount
            }
        }
    }
    fun getSumyByType(date: LocalDate): Flow<Map<TransactionType, Int>> {
        return fillterByMonth(date).map { list ->
            list.groupBy { it.type }
                .mapValues { entry ->
                    entry.value.sumOf { it.amount }
                }
        }
    }

    fun getSumyByCategories(date: LocalDate,type: TransactionType):Flow<Map<TransactionCategory,Int>>
    {
        return fillterByMonth(date).map{list->
            list.filter { it.type===type }.groupBy { it.category }.mapValues { entry->entry.value.sumOf { it.amount } }
        }
    }
    fun getTransactions(date: LocalDate,category: TransactionCategory):Flow<List<Transaction>>
    {
        return fillterByMonth(date).map { list->
            list.filter { it.category==category }
        }
    }
    fun fillterByMonth(date: LocalDate):Flow<List<Transaction>>
    {
        return allTransactions.map { list ->

            val filtered = list.filter {
                it.date.month == date.month && it.date.year == date.year
            }
            filtered
        }

    }
    fun deleteTransaction(transaction: Transaction)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTransaction(transaction)
            println("DEBUG: Pokouším se smazat transakci: ${transaction.name} s ID: ${transaction.id}")
        }

    }



}