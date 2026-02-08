package com.example.finamobileapp.models.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finamobileapp.models.MonthlyGoal // Cesta k tvé nové entitě
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Tohle zajistí ten "Upsert"
    suspend fun insertOrUpdateGoal(goal: MonthlyGoal)

    @Query("SELECT * FROM monthly_goals WHERE year = :year AND month = :month")
    fun getGoalForMonth(year: Int, month: Int): Flow<MonthlyGoal?>

    @Query("SELECT * FROM monthly_goals")
    fun getAllGoals(): Flow<List<MonthlyGoal>>
}