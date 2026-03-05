package com.example.finamobileapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.model.database.TransactionDatabase
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CategoryDetailViewModel(
    application: Application,
    categoryName: String
) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository
    private val categoryEnum = TransactionCategory.valueOf(categoryName)

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    val uiState: StateFlow<CategoryDetailUiState> = _uiState.asStateFlow()

    init {
        val transactionDao = TransactionDatabase.getDatabase(application).transactionDao()
        transactionRepository = TransactionRepository(transactionDao)
        viewModelScope.launch {
            transactionRepository.getTransactions(LocalDate.now(), categoryEnum)
                .collect { list -> _uiState.update { it.copy(transactions = list) } }
        }
    }

    // --- Funkce stavu viditelnosti a Modalů ---

    fun toggleExpand(transactionId: Long) {
        _uiState.update {
            it.copy(expandedTransactionId = if (it.expandedTransactionId == transactionId) null else transactionId)
        }
    }

    fun openOptions(transaction: Transaction) {
        _uiState.update {
            it.copy(selectedTransaction = transaction, isOptionsOpen = true)
        }
    }

    fun openDeleteForm(isRecurring: Boolean) {
        _uiState.update {
            it.copy(isDeleteFormOpen = true, isRecurringAction = isRecurring, isOptionsOpen = false)
        }
    }

    fun openUpdateForm(isRecurring: Boolean) {
        _uiState.update {
            it.copy(isUpdateFormOpen = true, isRecurringAction = isRecurring, isOptionsOpen = false)
        }
    }

    fun closeAllModals() {
        _uiState.update {
            it.copy(
                isOptionsOpen = false,
                isDeleteFormOpen = false,
                isUpdateFormOpen = false,
                selectedTransaction = null,
                isRecurringAction = false,
            )
        }
    }

    // --- Funkce stavu logiky (Loading) ---

    fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    // --- Funkce pro Mazání (Zůstávají zde, protože ovlivňují aktuální list) ---

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            transactionRepository.deleteTransaction(transaction)
            withContext(Dispatchers.Main) {
                closeAllModals()
                setLoading(false)
            }
        }
    }

    fun deleteRecurring(groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            transactionRepository.deleteRecurring(groupId, LocalDate.now())
            withContext(Dispatchers.Main) {
                closeAllModals()
                setLoading(false)
            }
        }
    }
}