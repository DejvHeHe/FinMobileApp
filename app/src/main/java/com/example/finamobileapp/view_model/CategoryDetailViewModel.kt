package com.example.finamobileapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.model.database.TransactionDatabase
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.entities.enums.TransactionType
import com.example.finamobileapp.model.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

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


    //Funkce stavu viditelnosti

    fun toggleExpand(transactionId: Long) {
        _uiState.update {
            it.copy(
                expandedTransactionId = if (it.expandedTransactionId == transactionId) null else transactionId
            )
        }
    }

    //    val expandedCategory: Boolean = false,
//    val showStartDatePicker: Boolean = false,
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

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.updateTransaction(transaction)
        }
    }

    fun deleteRecurring(groupId: String) {


        viewModelScope.launch(Dispatchers.IO) {

            transactionRepository.deleteRecurring(groupId, LocalDate.now())

        }
    }

    fun updateRecurring(
        groupId: String,
        name: String,
        amount: Int,
        category: TransactionCategory,
        type: TransactionType,
        description: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.updateRecurring(
                groupId, name, amount, category, type, description
            )
        }
    }
}