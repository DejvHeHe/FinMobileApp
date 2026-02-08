package com.example.finamobileapp.models.repository

import com.example.finamobileapp.models.MonthlyGoal
import com.example.finamobileapp.models.dao.GoalDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class MonthlyGoalRepository(private val goalDao: GoalDao) {


    fun getGoalForMonth(year: Int, month: Int): Flow<MonthlyGoal?> {
        return goalDao.getGoalForMonth(year, month)
    }


    fun getCurrentMonthGoal(): Flow<MonthlyGoal?> {
        val now = LocalDate.now()
        return goalDao.getGoalForMonth(now.year, now.monthValue)
    }


    suspend fun setGoal(goal: MonthlyGoal) {
        goalDao.insertOrUpdateGoal(goal)
    }
}