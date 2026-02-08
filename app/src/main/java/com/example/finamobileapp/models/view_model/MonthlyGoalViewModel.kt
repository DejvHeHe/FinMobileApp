package com.example.finamobileapp.models.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.models.MonthlyGoal
import com.example.finamobileapp.models.database.TransactionDatabase
import com.example.finamobileapp.models.repository.MonthlyGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MonthlyGoalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MonthlyGoalRepository

    init {

        val goalDao = TransactionDatabase.getDatabase(application).goalDao()
        repository = MonthlyGoalRepository(goalDao)
    }


    fun getGoalForMonth(year: Int, month: Int): Flow<MonthlyGoal?> {
        return repository.getGoalForMonth(year, month)
    }


    fun getCurrentMonthGoal(): Flow<MonthlyGoal?> {
        val now = LocalDate.now()
        return repository.getGoalForMonth(now.year, now.monthValue)
    }



    fun setGoal(goal: MonthlyGoal) {
        viewModelScope.launch {
            repository.setGoal(goal)
        }
    }
}