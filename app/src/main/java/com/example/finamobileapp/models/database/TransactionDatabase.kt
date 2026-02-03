package com.example.finamobileapp.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finamobileapp.database.TransactionDao
import com.example.finamobileapp.models.Converters
import com.example.finamobileapp.models.Transaction


@Database(entities = [Transaction::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TransactionDatabase: RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object{
        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        fun getDatabase(context: Context): TransactionDatabase{
            val tempInstance=INSTANCE
            if(tempInstance!=null)
            {
                return  tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction_database"
                ).build()
                INSTANCE=instance
                return instance
            }
        }

    }




}