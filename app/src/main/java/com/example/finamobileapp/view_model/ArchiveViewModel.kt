package com.example.finamobileapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.model.database.TransactionDatabase
import com.example.finamobileapp.model.entities.enums.TransactionType
import com.example.finamobileapp.model.repository.TransactionRepository
import com.example.finamobileapp.view_model.interfaces.DonutGraphActions
import com.example.finamobileapp.view_model.uiState.ArchiveUiState
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
            is DonutGraphActions.YearMonthMinus -> yearMonthPlus()
            is DonutGraphActions.YearMonthPlus -> yearMonthMinus()
        }
    }

    private fun yearMonthPlus() {
        _uiState.update { it.copy(selectedYearMonth = it.selectedYearMonth.plusMonths(1)) }
    }

    private fun yearMonthMinus() {
        _uiState.update { it.copy(selectedYearMonth = it.selectedYearMonth.minusMonths(1)) }
    }

    private fun loadExpenses() {
        val date = _uiState.value.selectedYearMonth
        val localeDate = date.atDay(1)
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    donutGraphExpense = repository.getSumyByCategories(
                        localeDate,
                        TransactionType.EXPENSE
                    )
                )
            }
        }
    }


}