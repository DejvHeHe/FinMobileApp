package com.example.finamobileapp.models.repository

import com.example.finamobileapp.models.BuyIdeas
import com.example.finamobileapp.models.dao.BuyIdeasDao
import kotlinx.coroutines.flow.Flow

class BuyIdeasRepository(private val buyIdeasDao: BuyIdeasDao) {

    val allBuyIdeas: Flow<List<BuyIdeas>> = buyIdeasDao.getBuyIdeas()

    suspend fun addBuyIdea(buyIdea: BuyIdeas) {
        buyIdeasDao.addBuyIdea(buyIdea)

    }


    suspend fun deleteBuyIdea(buyIdea: BuyIdeas) {
        buyIdeasDao.deleteBuyIdea(buyIdea)

    }


    suspend fun updateBuyIdea(buyIdea: BuyIdeas) {
        buyIdeasDao.updateBuyIdea(buyIdea)

    }


}