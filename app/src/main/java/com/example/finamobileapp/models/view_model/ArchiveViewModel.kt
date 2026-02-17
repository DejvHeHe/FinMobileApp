package com.example.finamobileapp.models.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.TransactionType
import com.example.finamobileapp.models.database.TransactionDatabase
import com.example.finamobileapp.models.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class ArchiveViewModel(application: Application): AndroidViewModel(application) {

    private val allTransactions : Flow<List<Transaction>>
    private val repository: TransactionRepository

    init{
        val transactionDao= TransactionDatabase.getDatabase(application).transactionDao()
        repository= TransactionRepository(transactionDao)
        allTransactions=repository.allTransactions
    }

    fun getSumyByCategories(date: LocalDate, type: TransactionType): Flow<Map<TransactionCategory, Int>> =
        repository.getSumyByCategories(date,type)
}