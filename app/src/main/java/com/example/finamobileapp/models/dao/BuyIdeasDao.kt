package com.example.finamobileapp.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finamobileapp.models.BuyIdeas
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyIdeasDao {

    @Insert
    suspend fun addBuyIdea(buyIdea: BuyIdeas)

    @Delete
    suspend fun deleteBuyIdea(buyIdea: BuyIdeas)

    @Update
    suspend fun updateBuyIdea(buyIdea: BuyIdeas)

    @Query("SELECT * FROM buy_ideas_table")
    fun getBuyIdeas(): Flow<List<BuyIdeas>>
}