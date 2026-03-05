package com.example.finamobileapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {


    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun toggleCreateForm() {
        _uiState.update { it.copy(isCreateFormOpen = !it.isCreateFormOpen) }
    }


}