package com.example.finamobileapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.model.database.TransactionDatabase
import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.MonthlyGoal
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.entities.enums.TransactionType
import com.example.finamobileapp.model.repository.BuyIdeasRepository
import com.example.finamobileapp.model.repository.MonthlyGoalRepository
import com.example.finamobileapp.model.repository.TransactionRepository
import com.example.finamobileapp.view_model.uiState.DashboardUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository
    private val buyIdeaRepository: BuyIdeasRepository

    private val monthlyGoalRepository: MonthlyGoalRepository

    init {
        val transactionDao = TransactionDatabase.getDatabase(application).transactionDao()
        val buyIdeaDao = TransactionDatabase.getDatabase(application).buyIdeaDao()
        val monthlyGoalDao = TransactionDatabase.getDatabase(application).goalDao()
        transactionRepository = TransactionRepository(transactionDao)
        buyIdeaRepository = BuyIdeasRepository(buyIdeaDao)
        monthlyGoalRepository = MonthlyGoalRepository(monthlyGoalDao)
    }

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()


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


    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.addSmartTransaction(transaction)
        }
    }


    fun addBuyIdea(buyIdea: BuyIdeas) {
        viewModelScope.launch(Dispatchers.IO) {
            buyIdeaRepository.addBuyIdea(buyIdea)
        }

    }

    fun getBuyIdeas(): Flow<List<BuyIdeas>> {
        return buyIdeaRepository.allBuyIdeas

    }

    fun deleteBuyIdea(buyIdea: BuyIdeas) {
        viewModelScope.launch(Dispatchers.IO) {
            buyIdeaRepository.deleteBuyIdea(buyIdea)
        }

    }

    fun updateBuyIdea(buyIdea: BuyIdeas) {
        viewModelScope.launch(Dispatchers.IO)
        {
            buyIdeaRepository.updateBuyIdea(buyIdea)

        }

    }

    fun getCurrentMonthGoal(): Flow<MonthlyGoal?> {
        val now = LocalDate.now()
        return monthlyGoalRepository.getGoalForMonth(now.year, now.monthValue)
    }


    fun setGoal(goal: MonthlyGoal) {
        viewModelScope.launch {
            monthlyGoalRepository.setGoal(goal)
        }
    }


}