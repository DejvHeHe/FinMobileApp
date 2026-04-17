package com.example.finamobileapp.view_model

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.model.DonutSegment
import com.example.finamobileapp.model.database.TransactionDatabase
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.entities.enums.TransactionType
import com.example.finamobileapp.model.repository.TransactionRepository
import com.example.finamobileapp.view_model.interfaces.DonutGraphActions
import com.example.finamobileapp.view_model.uiState.ArchiveUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArchiveViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState: StateFlow<ArchiveUiState> = _uiState.asStateFlow()

    private val repository: TransactionRepository

    init {
        val transactionDao = TransactionDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        loadExpenses()

    }

    fun onDonutGraphActions(action: DonutGraphActions) {
        when (action) {
            is DonutGraphActions.LoadExpenses -> loadExpenses()
            is DonutGraphActions.YearMonthMinus -> yearMonthMinus()
            is DonutGraphActions.YearMonthPlus -> yearMonthPlus()
        }
    }

    private fun yearMonthPlus() {
        _uiState.update { it.copy(selectedYearMonth = it.selectedYearMonth.plusMonths(1)) }
        loadExpenses()
    }

    private fun yearMonthMinus() {
        _uiState.update { it.copy(selectedYearMonth = it.selectedYearMonth.minusMonths(1)) }
        loadExpenses()
    }

    private var expensesJob: Job? = null

    private fun loadExpenses() {
        val date = _uiState.value.selectedYearMonth
        val localeDate = date.atDay(1)


        expensesJob?.cancel()

        expensesJob = viewModelScope.launch {
            repository.getSumyByCategories(localeDate, TransactionType.EXPENSE)
                .collect { summaryMap ->
                    val totalSum = summaryMap.values.sum()

                    val segments = summaryMap.map { (category, amount) ->

                        DonutSegment(
                            category = category,
                            amount = amount,
                            sweepAngle = (amount.toFloat() / totalSum) * 360f,
                            color = category.colorLogic()
                        )

                    }
                    _uiState.update { it.copy(donutGraphExpense = segments) }
                }
        }


    }

    private val colorLogic: TransactionCategory.() -> Color = {
        when (this) {
            TransactionCategory.SAVINGS -> Color(0xFF4CAF50)
            TransactionCategory.FUN -> Color(0xFFFFEB3B)
            TransactionCategory.INVESTMENT -> Color(0xFF8BC34A)
            TransactionCategory.FOOD -> Color(0xFFFF9800)
            TransactionCategory.RENT -> Color(0xFF2196F3)
            TransactionCategory.CLOTHES -> Color(0xFF9C27B0)
            TransactionCategory.DRUGS -> Color(0xFFE91E63)
            TransactionCategory.VET -> Color(0xFF795548)
            TransactionCategory.PRESENT -> Color(0xFFFF5722)
            TransactionCategory.HEALTH -> Color(0xFF00BCD4)
            TransactionCategory.GYM -> Color(0xFF3F51B5)
            TransactionCategory.OTHER -> Color(0xFF9E9E9E)
            else -> Color(0xFF000000)
        }
    }


}
