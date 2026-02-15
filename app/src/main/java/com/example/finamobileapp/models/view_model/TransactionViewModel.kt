package com.example.finamobileapp.models.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.models.TransactionAccountType
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
import java.time.temporal.ChronoUnit


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

            if(transaction.category==TransactionCategory.TRANSFER)
            {
                if(transaction.accountType==TransactionAccountType.REGULAR)
                {
                    repository.addTransaction(transaction.copy(type = TransactionType.INCOME, accountType = TransactionAccountType.SAVINGS, category = TransactionCategory.SAVINGS))
                    repository.addTransaction(transaction)

                }
                else{
                    repository.addTransaction(transaction.copy(type=TransactionType.INCOME, accountType = TransactionAccountType.REGULAR))
                    repository.addTransaction(transaction)


                }


            }
            else{
                repository.addTransaction(transaction)
            }

        }
    }
    fun addRecurring(transaction: Transaction,endDate: LocalDate)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            val startDate=transaction.date
            val monthsToCreate=ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), endDate.withDayOfMonth(1))
            for (i in 0..monthsToCreate)
            {
                val transactionCopy=transaction.copy(date=startDate.plusMonths(i))
                repository.addTransaction(transactionCopy)
            }

        }

    }

    fun getBalance(date: LocalDate,accountType: TransactionAccountType): Flow<Int> {
        return fillterByMonth(date).map { list ->
            list.filter { it.accountType==accountType }
            .sumOf {
                if (it.type == TransactionType.INCOME) it.amount else -it.amount
            }
        }
    }
    fun getSumyByType(date: LocalDate): Flow<Map<TransactionType, Int>> {
        return fillterByMonth(date).map { list ->
            list.filter{it.category!=TransactionCategory.SAVINGS && it.category!=TransactionCategory.TRANSFER}
            .groupBy { it.type }
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
    fun getSumForCategory(date: LocalDate, category: TransactionCategory): Flow<Int> {
        return fillterByMonth(date).map { list ->
            list.filter { it.category == category }
                .sumOf { it.amount }
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
    fun updateTransaction(transaction: Transaction)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransaction(transaction)
            println("DEBUG: Pokouším se upravit transakci: ${transaction.name} s ID: ${transaction.id}")
        }

    }

    fun deleteRecurring(groupId: String, today: LocalDate)
    {

        viewModelScope.launch (Dispatchers.IO){
            repository.deleteRecurring(groupId,today)
            println("DEBUG:Mažu všechny transakce v řadě")
        }
    }
    fun updateRecurring(groupId: String, name: String, amount: Int, category: TransactionCategory, type: TransactionType, description: String)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.updateRecurring(groupId,name,amount,category,type,description)
            println("DEBUG:Updatuju řadu transakcí")
        }
    }





}