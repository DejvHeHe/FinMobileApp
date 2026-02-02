package com.example.finamobileapp.models

import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.UUID

class Converters {
    // Pro LocalDate
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? = date?.toEpochDay()


    // Pro TransactionCategory (Enum)
    @TypeConverter
    fun fromCategory(category: TransactionCategory): String = category.name

    @TypeConverter
    fun toCategory(value: String): TransactionCategory = TransactionCategory.valueOf(value)

    // Pro TransactionType (Enum)
    @TypeConverter
    fun fromType(type: TransactionType): String = type.name

    @TypeConverter
    fun toType(value: String): TransactionType = TransactionType.valueOf(value)
}