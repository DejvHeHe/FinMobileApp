package com.example.finamobileapp.model.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finamobileapp.model.entities.MonthlyGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGoal(goal: MonthlyGoal)

    @Query("SELECT * FROM monthly_goals WHERE year = :year AND month = :month")
    fun getGoalForMonth(year: Int, month: Int): Flow<MonthlyGoal?>

    @Query("SELECT * FROM monthly_goals")
    fun getAllGoals(): Flow<List<MonthlyGoal>>
}