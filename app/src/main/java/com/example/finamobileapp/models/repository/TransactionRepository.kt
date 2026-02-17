package com.example.finamobileapp.models.repository

import com.example.finamobileapp.database.TransactionDao
import com.example.finamobileapp.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    
    suspend fun addSmartTransaction(transaction: Transaction) {
        if (transaction.category == TransactionCategory.TRANSFER) {
            if (transaction.accountType == TransactionAccountType.REGULAR) {
                transactionDao.addTransaction(transaction.copy(type = TransactionType.INCOME, accountType = TransactionAccountType.SAVINGS, category = TransactionCategory.SAVINGS))
                transactionDao.addTransaction(transaction)
            } else {
                transactionDao.addTransaction(transaction.copy(type = TransactionType.INCOME, accountType = TransactionAccountType.REGULAR))
                transactionDao.addTransaction(transaction)
            }
        } else {
            transactionDao.addTransaction(transaction)
        }
    }

    suspend fun addRecurringTransactions(transaction: Transaction, endDate: LocalDate) {
        val startDate = transaction.date
        val monthsToCreate = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), endDate.withDayOfMonth(1))
        for (i in 0..monthsToCreate) {
            transactionDao.addTransaction(transaction.copy(date = startDate.plusMonths(i)))
        }
    }

    
    fun filterByMonth(date: LocalDate): Flow<List<Transaction>> {
        return allTransactions.map { list ->
            list.filter { it.date.month == date.month && it.date.year == date.year }
        }
    }

    fun getBalance(date: LocalDate, accountType: TransactionAccountType): Flow<Int> {
        return filterByMonth(date).map { list ->
            list.filter { it.accountType == accountType }
                .sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
        }
    }

    fun getSumyByType(date: LocalDate): Flow<Map<TransactionType, Int>> {
        return filterByMonth(date).map { list ->
            list.filter { it.category != TransactionCategory.SAVINGS && it.category != TransactionCategory.TRANSFER }
                .groupBy { it.type }
                .mapValues { it.value.sumOf { t -> t.amount } }
        }
    }

    fun getSumyByCategories(date: LocalDate, type: TransactionType): Flow<Map<TransactionCategory, Int>> {
        return filterByMonth(date).map { list ->
            list.filter { it.type == type }
                .groupBy { it.category }
                .mapValues { it.value.sumOf { t -> t.amount } }
        }
    }

    fun getSumForCategory(date: LocalDate, category: TransactionCategory): Flow<Int> {
        return filterByMonth(date).map { list ->
            list.filter { it.category == category }.sumOf { it.amount }
        }
    }

    fun getTransactions(date: LocalDate, category: TransactionCategory): Flow<List<Transaction>> {
        return filterByMonth(date).map { list ->
            list.filter { it.category == category }
        }
    }

    
    suspend fun deleteTransaction(transaction: Transaction) = transactionDao.deleteTransaction(transaction)
    suspend fun updateTransaction(transaction: Transaction) = transactionDao.updateTransaction(transaction)
    suspend fun deleteRecurring(groupId: String, today: LocalDate) = transactionDao.deleteRecurring(groupId, today)
    suspend fun updateRecurring(groupId: String, name: String, amount: Int, category: TransactionCategory, type: TransactionType, description: String) =
        transactionDao.updateRecurring(groupId, name, amount, category, type, description)
}