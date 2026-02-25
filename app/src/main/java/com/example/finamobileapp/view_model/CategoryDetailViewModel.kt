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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class CategoryDetailViewModel(
    application: Application,
    categoryName: String
) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository
    private val categoryEnum = TransactionCategory.valueOf(categoryName)

    init {
        val transactionDao = TransactionDatabase.getDatabase(application).transactionDao()
        transactionRepository = TransactionRepository(transactionDao)
        viewModelScope.launch {
            transactionRepository.getTransactions(LocalDate.now(), categoryEnum)
                .collect { list -> _uiState.update { it.copy(transactions = list) } }
        }
    }

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    val uiState: StateFlow<CategoryDetailUiState> = _uiState.asStateFlow()

    //Funkce proměných
    fun setName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun setAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun setDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun setSelectedOption(selectedOption: String) {
        _uiState.update { it.copy(selectedOption = selectedOption) }
    }

    fun setDate(millis: Long?) {
        _uiState.update { it.copy(selectedDateMillis = millis) }
    }


    //Funkce stavu viditelnosti

    fun toggleExpand(transactionId: Long) {
        _uiState.update {
            it.copy(
                expandedTransactionId = if (it.expandedTransactionId == transactionId) null else transactionId
            )
        }
    }


    fun toggleExpandCategory() {
        _uiState.update { it.copy(expandedCategory = !it.expandedCategory) }
    }

    fun toggleStartDatePicker() {
        _uiState.update { it.copy(showStartDatePicker = !it.showStartDatePicker) }
    }

    fun openOptions(transaction: Transaction) {
        _uiState.update {
            it.copy(
                selectedTransaction = transaction,
                isOptionsOpen = true
            )
        }
    }


    fun openDeleteForm(isRecurring: Boolean) {
        _uiState.update {
            it.copy(
                isDeleteFormOpen = true,
                isRecurringAction = isRecurring,
                isOptionsOpen = false
            )
        }
    }


    fun openUpdateForm(isRecurring: Boolean) {
        _uiState.update {
            val transaction = it.selectedTransaction
            it.copy(
                isUpdateFormOpen = true,
                isRecurringAction = isRecurring,
                isOptionsOpen = false,
                name = transaction?.name ?: "",
                amount = transaction?.amount?.toString() ?: "",
                description = transaction?.description ?: "",
                selectedOption = transaction?.category?.name ?: ""
            )
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

    //Funkce formulaře update
    fun saveUpdate() {
        val state = _uiState.value
        val original = state.selectedTransaction ?: return


        val categoryEnum = TransactionCategory.entries.find { it.name == state.selectedOption }
            ?: original.category


        val selectedDate = state.selectedDateMillis?.let { millis ->
            Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } ?: original.date


        val updatedTransaction = original.copy(
            name = state.name,
            amount = state.amount.toIntOrNull() ?: original.amount,
            category = categoryEnum,
            type = categoryEnum.type,
            date = selectedDate,
            description = state.description
        )


        viewModelScope.launch(Dispatchers.IO) {
            if (state.isRecurringAction && updatedTransaction.groupId != null) {
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

            launch(Dispatchers.Main) {
                closeAllModals()
            }
        }
    }

    //Funkce stavu logiky

    fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }


    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            transactionRepository.deleteTransaction(transaction)

            viewModelScope.launch(Dispatchers.Main)
            {
                closeAllModals()
                setLoading(false)

            }


        }

    }


    fun deleteRecurring(groupId: String) {


        viewModelScope.launch(Dispatchers.IO) {

            transactionRepository.deleteRecurring(groupId, LocalDate.now())

        }
    }


}