package com.example.finamobileapp.model.repository

import com.example.finamobileapp.model.database.dao.GoalDao
import com.example.finamobileapp.model.entities.MonthlyGoal
import kotlinx.coroutines.flow.Flow


class MonthlyGoalRepository(private val goalDao: GoalDao) {


    fun getGoalForMonth(year: Int, month: Int): Flow<MonthlyGoal?> {
        return goalDao.getGoalForMonth(year, month)
    }


    suspend fun setGoal(goal: MonthlyGoal) {
        goalDao.insertOrUpdateGoal(goal)
    }
}