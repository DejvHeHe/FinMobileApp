package com.example.finamobileapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.model.database.TransactionDatabase
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

class CreateFormViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository

    init {
        val transactionDao = TransactionDatabase.getDatabase(application).transactionDao()
        transactionRepository = TransactionRepository(transactionDao)
    }

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    fun setName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun setAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun setDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun selectCategory(category: TransactionCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun selectAccountType(accountType: TransactionAccountType) {
        _uiState.update { it.copy(selectedAccountType = accountType) }
    }

    fun setStartDate(millis: Long?) {
        _uiState.update { it.copy(startDateMillis = millis) }
    }

    fun setEndDate(millis: Long?) {
        _uiState.update { it.copy(endDateMillis = millis) }
    }

    fun toggleIsRecurring() {
        _uiState.update { it.copy(isRecurring = !it.isRecurring) }

    }


    fun toggleCategoryExpand() {
        _uiState.update { it.copy(isCategoryExpanded = !it.isCategoryExpanded) }
    }

    fun toggleAccountTypeExpand() {
        _uiState.update { it.copy(isAccountTypeExpanded = !it.isAccountTypeExpanded) }
    }

    fun toggleStartDatePicker() {
        _uiState.update { it.copy(showStartDatePicker = !it.showStartDatePicker) }
    }

    fun toggleEndDatePicker() {
        _uiState.update { it.copy(showEndDatePicker = !it.showEndDatePicker) }
    }

    fun onConfirmClicked(onSuccess: () -> Unit) {
        val s = _uiState.value

        // 1. Transformace (Parsing) - z "bordelu" na čisté typy
        val amountInt = s.amount.toIntOrNull() ?: 0
        val category = s.selectedCategory
        val accountType = s.selectedAccountType

        val startDate = s.startDateMillis?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        } ?: LocalDate.now()

        val endDate = s.endDateMillis?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        } ?: startDate.plusMonths(1)

        // 2. Validace (Business Rules)
        val isFormValid = s.name.isNotBlank() &&
                amountInt > 0 &&
                category != null &&
                accountType != null &&
                (!s.isRecurring || !endDate.isBefore(startDate))

        // 3. Akce (pokud je vše validní)
        if (isFormValid) {
            viewModelScope.launch {
                // Tady už posíláš "Validated" věci ve správném formátu
                val newTransaction = Transaction(
                    name = s.name.trim(),
                    amount = amountInt,
                    type = category.type,
                    category = category,
                    accountType = accountType,
                    date = startDate,
                    description = s.description.trim(),
                    groupId = if (s.isRecurring) UUID.randomUUID().toString() else null
                )

                if (s.isRecurring) {
                    transactionRepository.addRecurringTransactions(newTransaction, endDate)
                } else {
                    transactionRepository.addSmartTransaction(newTransaction)
                }

                resetForm()
                onSuccess()
            }
        } else {

            println("Validation failed")
        }
    }

    fun resetForm() {
        _uiState.update {
            FormUiState()
        }
    }


}