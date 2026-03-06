package com.example.finamobileapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.model.database.TransactionDatabase
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.repository.TransactionRepository
import com.example.finamobileapp.view_model.uiState.FormUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId

class UpdateFormViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository

    init {
        val transactionDao = TransactionDatabase.getDatabase(application).transactionDao()
        transactionRepository = TransactionRepository(transactionDao)
    }

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    // Mapování existujících dat do formuláře
    fun loadTransactionData(transaction: Transaction, isRecurring: Boolean) {
        _uiState.update {
            it.copy(
                name = transaction.name,
                amount = transaction.amount.toString(),
                description = transaction.description,
                selectedCategory = transaction.category,
                startDateMillis = transaction.date.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli(),
                isRecurring = isRecurring
            )
        }
    }

    // Settery pro UI komponenty
    fun setName(name: String) = _uiState.update { it.copy(name = name) }
    fun setAmount(amount: String) = _uiState.update { it.copy(amount = amount) }
    fun setDescription(description: String) = _uiState.update { it.copy(description = description) }
    fun setCategory(category: TransactionCategory) =
        _uiState.update { it.copy(selectedCategory = category) }

    fun setDate(millis: Long?) = _uiState.update { it.copy(startDateMillis = millis) }

    fun toggleCategoryExpand() =
        _uiState.update { it.copy(isCategoryExpanded = !it.isCategoryExpanded) }

    fun toggleDatePicker() =
        _uiState.update { it.copy(showStartDatePicker = !it.showStartDatePicker) }

    fun saveUpdate(original: Transaction, isRecurringAction: Boolean, onSuccess: () -> Unit) {
        val state = _uiState.value

        val categoryEnum = state.selectedCategory ?: original.category
        val selectedDate = state.startDateMillis?.let { millis ->
            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
        } ?: original.date

        val updatedTransaction = original.copy(
            name = state.name.trim(),
            amount = state.amount.toIntOrNull() ?: original.amount,
            category = categoryEnum,
            type = categoryEnum.type,
            date = selectedDate,
            description = state.description.trim()
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (isRecurringAction && updatedTransaction.groupId != null) {
                transactionRepository.updateRecurring(
                    groupId = updatedTransaction.groupId,
                    name = updatedTransaction.name,
                    amount = updatedTransaction.amount,
                    category = updatedTransaction.category,
                    type = updatedTransaction.type,
                    description = updatedTransaction.description
                )
            } else {
                transactionRepository.updateTransaction(updatedTransaction)
            }

            withContext(Dispatchers.Main) {
                reset()
                onSuccess()
            }
        }
    }

    fun reset() {
        _uiState.update { FormUiState() }
    }
}