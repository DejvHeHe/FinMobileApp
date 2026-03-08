package com.example.finamobileapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.model.database.TransactionDatabase
import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.MonthlyGoal
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.FormMode
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository
    private val buyIdeaRepository: BuyIdeasRepository
    private val monthlyGoalRepository: MonthlyGoalRepository

    init {
        val db = TransactionDatabase.getDatabase(application)
        transactionRepository = TransactionRepository(db.transactionDao())
        buyIdeaRepository = BuyIdeasRepository(db.buyIdeaDao())
        monthlyGoalRepository = MonthlyGoalRepository(db.goalDao())
    }

    private val today = LocalDate.now()
    private val _buyIdeaAction = MutableStateFlow(BuyIdeaAction())

    data class BuyIdeaAction(
        val isOpen: Boolean = false,
        val mode: FormMode = FormMode.CREATE,
        val selectedIdea: BuyIdeas? = null
    )

    val buyIdeaState: StateFlow<BuyIdeaAction> = _buyIdeaAction.asStateFlow()

    val uiState: StateFlow<DashboardUiState> = combine(
        combine(
            transactionRepository.getBalance(today, TransactionAccountType.REGULAR),
            transactionRepository.getBalance(today, TransactionAccountType.SAVINGS)

        ) { reg, sav -> reg to sav },
        transactionRepository.getSumyByType(today),
        buyIdeaRepository.allBuyIdeas,
        monthlyGoalRepository.getGoalForMonth(today.year, today.monthValue),
        transactionRepository.getSumForCategory(today, TransactionCategory.INVESTMENT)
    ) { balances, typeSums, ideas, goal, invested ->

        DashboardUiState(
            balanceRegular = balances.first,
            balanceSavings = balances.second,
            typeSums = typeSums,
            buyIdeas = ideas,
            currentGoal = goal,
            currentlyInvested = invested,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )


    fun getSumyByCategories(type: TransactionType): Flow<Map<TransactionCategory, Int>> =
        transactionRepository.getSumyByCategories(today, type)


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

    fun deleteBuyIdea(buyIdea: BuyIdeas) {
        viewModelScope.launch(Dispatchers.IO) {
            buyIdeaRepository.deleteBuyIdea(buyIdea)
        }
    }

    fun updateBuyIdea(buyIdea: BuyIdeas) {
        viewModelScope.launch(Dispatchers.IO) {
            buyIdeaRepository.updateBuyIdea(buyIdea)
        }
    }

    fun setGoal(goal: MonthlyGoal) {
        viewModelScope.launch(Dispatchers.IO) {
            monthlyGoalRepository.setGoal(goal)
        }
    }

    fun setBuyIdeaSheet(isOpen: Boolean) {
        _buyIdeaAction.update { it.copy(isOpen = isOpen) }
    }

    fun prepareUpdate(idea: BuyIdeas) {
        _buyIdeaAction.update {
            it.copy(selectedIdea = idea, mode = FormMode.UPDATE, isOpen = true)
        }
    }

    fun prepareCreate() {
        _buyIdeaAction.update {
            it.copy(selectedIdea = null, mode = FormMode.CREATE, isOpen = true)
        }
    }


}